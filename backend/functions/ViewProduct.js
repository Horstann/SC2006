const { getFirestore } = require('firebase-admin/firestore');

class ViewProduct {
	async ExecuteCommand(cmdData, res) {
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
				"desc": doc.data().Description,
				"picUrl": doc.data().Picture,
				"sellerLoc": doc.data().SellerLocation,
				"sellerAddr": doc.data().SellerAddress
			});
		}
	}
}

module.exports = ViewProduct;
