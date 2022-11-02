const { getFirestore, Timestamp } = require('firebase-admin/firestore');

class ClosingTimeChecker {
	async ExecuteCommand(cmdData, acc, res) {
		const db = getFirestore();
		const productRef = db.collection("Product").doc(cmdData.productId);
		const doc = await productRef.get();
		if (!doc.exists) res.json({"status": 7});

        let isClosed = false;
		if (doc.data().ClosingTime.toMillis() < Timestamp.now().toMillis()) isClosed = true;

		res.json({
			"status": 0,
			"isClosed": isClosed
		});
	}
}

module.exports = ClosingTimeChecker;