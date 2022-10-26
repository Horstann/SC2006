const { getFirestore } = require('firebase-admin/firestore');

class BuyerProductsLoader {
	async ExecuteCommand(cmdData, acc, res) {
		let buyerLat = acc.data().HomeLocation.latitude;
		let buyerLong = acc.data().HomeLocation.longtitude;

		const db = getFirestore();
		const productRefs = db.collection('Product');
		const snapshot = await productRefs.get();

		if (acc.data().BoughtProducts) res.json({"status": 7});

		let products = [];
		snapshot.forEach(async doc => {
			if (doc.id in acc.data().BoughtProducts) {
				const sellerId = doc.data().Seller.id;
				const sellerRef = db.collection("User").doc(sellerId);
				const sellerDoc = await sellerRef.get();
				let sellerLat = sellerDoc.data().HomeLocation.latitude;
				let sellerLong = sellerDoc.data().HomeLocation.longtitude;
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
		});

		if (search_res.length == 0) res.json({"status": 7});
		search_res.sort(function(a, b){return a[1]-b[1]});

		function funct(item, index, arr) {
			arr[index] = item[0];
		}
		search_res.forEach(funct);

		res.json({
			"status": 0,
			"products": products
		});
	}
}

module.exports = BuyerProductsLoader;