const { getFirestore } = require('firebase-admin/firestore');

class QRCodeLoader {
	async ExecuteCommand(cmdData, acc, res) {
		if (cmdData.productId == undefined) {
			res.json({"status": 9});
			return;
		}

		const db = getFirestore();
		const productRef = db.collection("Product").doc(cmdData.productId);
		const doc = await productRef.get();
		if (!doc.exists) {
			res.json({"status": 7});
			return;
		}

		const sellerId = doc.data().Seller.id;
		const sellerRef = db.collection("User").doc(sellerId);
		const sellerDoc = await sellerRef.get();
		if (sellerDoc.data().QRCode == "") {
			res.json({"status": 10});
			return;
		}

		res.json({
			"status": 0,
			"qrcode": sellerDoc.data().QRCode
		});
	}
}

module.exports = QRCodeLoader;