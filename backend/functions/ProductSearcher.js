const { getFirestore, Timestamp } = require('firebase-admin/firestore');
const functions = require("firebase-functions");

// As query is too complex, it's unable to be pagenated.
// So function returns the full array of relevant products

class ProductSearcher {
	async ExecuteCommand(cmdData, acc, res) {
		if (cmdData.searchTerm == undefined) {
			res.json({"status": 9});
			return;
		}

		if (acc == null) {
			res.json({"status": 6});
			return;
		}

		let buyerLat = acc.data().HomeLocation.latitude;
		let buyerLong = acc.data().HomeLocation.longitude;

		const db = getFirestore();
		const productRefs = db.collection('Product');
		const snapshot = await productRefs.get();

		/*
		const buyerId = acc.id;
		const buyerRef = db.collection("User").doc(buyerId);
		const buyerDoc = await buyerRef.get();

		let buyerLat = buyerDoc.data().HomeLocation.latitude;
		let buyerLong = buyerDoc.data().HomeLocation.longtitude;
		let searchTerm = cmdData.searchTerm.trim().toLowerCase();
		*/

		let search_res = [];
		for (const doc of snapshot.docs) {
			if ((doc.data().Name.toLowerCase().includes(cmdData.searchTerm.toLowerCase())
			|| doc.data().Description.toLowerCase().includes(cmdData.searchTerm.toLowerCase()))
			&& doc.data().ClosingTime.toMillis() > Timestamp.now().toMillis()) {
				const sellerId = doc.data().Seller.id;
				const sellerRef = db.collection("User").doc(sellerId);
				const sellerDoc = await sellerRef.get();
				let sellerLat = sellerDoc.data().HomeLocation.latitude;
				let sellerLong = sellerDoc.data().HomeLocation.longitude;
				let distanceInKm = Math.sqrt(((buyerLat-sellerLat)*110.547)**2 + (111.320*Math.cos(buyerLong-sellerLong))**2);
				let timestamp = doc.data().ClosingTime;
				let date = timestamp.toDate();
				let date1 = [date.getDate().toString().padStart(2,'0'),(date.getMonth() + 1).toString().padStart(2,'0'),date.getFullYear(),].join('/')
				let date2 = date.getHours().toString().padStart(2,'0') + ":" + date.getMinutes().toString().padStart(2,'0')
				let dateString = date1 + " " + date2;

				search_res.push([{
					"productId": doc.id,
					"name": doc.data().Name,
					"totalUnits": doc.data().TotalUnits,
					"totalBought": doc.data().TotalBought,
					"priceThresholds": doc.data().PriceThresholds,
					"unitThresholds": doc.data().UnitThresholds,
					"closingTime": dateString,
					"desc": doc.data().Description,
					"pics": doc.data().Pictures,
					"distanceFromUser": distanceInKm
				}, distanceInKm]);
			}
		}

		if (search_res.length == 0) res.json({"status": 7});
		search_res.sort(function(a, b){return a[1]-b[1]});

		function funct(item, index, arr) {
			arr[index] = item[0];
		}
		search_res.forEach(funct);

		res.json({
			"status": 0,
			"products": search_res
		});
	}
}

module.exports = ProductSearcher;