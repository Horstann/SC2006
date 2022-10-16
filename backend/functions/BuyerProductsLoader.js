const { getFirestore } = require('firebase-admin/firestore');
const { admin } = require('firebase-admin');

class BuyerProductsLoader {
	async ExecuteCommand(cmdData, acc, res) {
		let buyerLat = acc.data().HomeLocation.latitude;
		let buyerLong = acc.data().HomeLocation.longtitude;

		const db = getFirestore();
		const productRefs = db.collection('Product');
		const snapshot = await productRefs.get();

		let products = [];
		snapshot.forEach(async doc => {
			if (doc.id in acc.BoughtProducts) {
				const sellerId = doc.data().Seller.id;
				const sellerRef = db.collection("User").doc(sellerId);
				const sellerDoc = await sellerRef.get();
				let sellerLat = sellerDoc.data().HomeLocation.latitude;
				let sellerLong = sellerDoc.data().HomeLocation.longtitude;
				let distanceInKm = geofire.distanceBetween([buyerLat, buyerLong], [sellerLat, sellerLong]);
				
				search_res.push([{
					"productId": doc.id,
					"name": doc.data().Name,
					"totalUnits": doc.data().TotalUnits,
					"totalBought": doc.data().TotalBought,
					"priceThresholds": doc.data().PriceThresholds,
					"unitThresholds": doc.data().UnitThresholds,
					"durationLeft": doc.data().ClosingTime,
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