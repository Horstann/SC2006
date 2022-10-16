const { getFirestore } = require('firebase-admin/firestore');

class ProductLoader {
	async ExecuteCommand(cmdData, acc, res) {
		let buyerLat = acc.data().HomeLocation.latitude;
		let buyerLong = acc.data().HomeLocation.longtitude;

		const db = getFirestore();
		const productRefs = db.collection('Product');
		const snapshot = await productRefs.get();

		const sellerId = doc.data().Seller.id;
		const sellerRef = db.collection("User").doc(sellerId);
		const sellerDoc = await sellerRef.get();
		let sellerLat = sellerDoc.data().HomeLocation.latitude;
		let sellerLong = sellerDoc.data().HomeLocation.longtitude;
		let distanceInKm = geofire.distanceBetween([buyerLat, buyerLong], [sellerLat, sellerLong]);

		let product_res = [];
		snapshot.forEach(doc => {
			product_res.push({
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
            });
		});

		if (product_res.length == 0) res.json({"status": 7});
		res.json({
			"status": 0,
			"products": product_res
		});
	}
}

module.exports = ProductLoader;