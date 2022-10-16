const { getFirestore } = require('firebase-admin/firestore');

class ProductViewer {
	async ExecuteCommand(cmdData, acc, res) {
		let buyerLat = acc.data().HomeLocation.latitude;
		let buyerLong = acc.data().HomeLocation.longtitude;

		// connect to firestore
		const db = getFirestore();
		const productRef = db.collection("Product").doc(cmdData.productId);
		const doc = await productRef.get();
		if (!doc.exists) res.json({"status": 7});

		const sellerId = doc.data().Seller.id;
		const sellerRef = db.collection("User").doc(sellerId);
		const sellerDoc = await sellerRef.get();
		let sellerLat = sellerDoc.data().HomeLocation.latitude;
		let sellerLong = sellerDoc.data().HomeLocation.longtitude;
		let distanceInKm = geofire.distanceBetween([buyerLat, buyerLong], [sellerLat, sellerLong]);

		res.json({
			"status": 0,
			"name": doc.data().Name,
			"totalUnits": doc.data().TotalUnits,
			"totalBought": doc.data().TotalBought,
			"priceThresholds": doc.data().PriceThresholds,
			"unitThresholds": doc.data().UnitThresholds,
			"closingTime": doc.data().ClosingTime,
			"desc": doc.data().Description,
			"pics": doc.data().Pictures,
			"sellerAddress": sellerDoc.data().HomeAddress,
			"sellerLat": sellerDoc.data().HomeLocation.latitude,
			"sellerLong": sellerDoc.data().HomeLocation.longitude,
			"distanceFromUser": distanceInKm
		});
	}
}

module.exports = ProductViewer;
