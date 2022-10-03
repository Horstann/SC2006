const { getFirestore } = require('firebase-admin/firestore');

class ProductViewer {
	async ExecuteCommand(cmdData, acc, res) {
		// connect to firestore
		const db = getFirestore();
		const productRef = db.collection("Product").doc(cmdData.productId);
		const doc = await productRef.get();
		if (!doc.exists) res.json({"status": 7});
		
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
			"sellerAddr": doc.data().Seller.data().HomeAddress,
			"sellerLat": doc.data().Seller.data().HomeLat,
			"sellerLong": doc.data().Seller.data().HomeLong
		});
	}
}

module.exports = ProductViewer;
