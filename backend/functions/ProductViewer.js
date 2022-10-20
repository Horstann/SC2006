const { getFirestore } = require('firebase-admin/firestore');

class ProductViewer {
	async ExecuteCommand(cmdData, acc, res) {
		let buyerLat = acc.data().HomeLocation.latitude;
		let buyerLong = acc.data().HomeLocation.longitude;

		// connect to firestore
		const db = getFirestore();
		const productRef = db.collection("Product").doc(cmdData.productId);
		const doc = await productRef.get();
		if (!doc.exists) res.json({"status": 7});

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

		res.json({
			"status": 0,
			"name": doc.data().Name,
			"totalUnits": doc.data().TotalUnits,
			"totalBought": doc.data().TotalBought,
			"priceThresholds": doc.data().PriceThresholds,
			"unitThresholds": doc.data().UnitThresholds,
			"closingTime": dateString,
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
