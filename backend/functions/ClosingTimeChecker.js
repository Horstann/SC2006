const { getFirestore } = require('firebase-admin/firestore');

class ProductViewer {
	async ExecuteCommand(cmdData, acc, res) {
		const db = getFirestore();
		const productRef = db.collection("Product").doc(cmdData.productId);
		const doc = await productRef.get();
		if (!doc.exists) res.json({"status": 7});

        let isClosed = false;
        if (doc.data().ClosingTime < admin.firestore.Timestamp.now()) isClosed = true;

		res.json({
			"status": 0,
			"isClosed": isClosed
		});
	}
}

module.exports = ProductViewer;
