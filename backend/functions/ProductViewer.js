const { getFirestore } = require('firebase-admin/firestore');

class ProductViewer {
	async ExecuteCommand(cmdData, acc, res) {
		// connect to firestore
		const db = getFirestore();
		const productRef = db.collection("Product").doc(cmdData.productId);
		const doc = await productRef.get();

		if (!doc.exists) {
			res.json({"status": 7});
		} else {
			res.json({
				"status": 0,
				"name": doc.data().Name,
				"totalUnits": doc.data().TotalUnits,
				"totalBought": doc.data().TotalBought,
				"priceThresholds": doc.data().PriceThresholds,
				"unitThresholds": doc.data().UnitThresholds,
				"durationLeft": doc.data().DurationLeft,
				"desc": doc.data().Description,
				"picUrl": doc.data().Picture,
				"sellerLoc": doc.data().SellerLocation,
				"sellerAddr": doc.data().SellerAddress
			});
		}
	}
}

module.exports = ProductViewer;
