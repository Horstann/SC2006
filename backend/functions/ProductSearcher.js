const { getFirestore } = require('firebase-admin/firestore');
const { getAuth } = require("firebase/auth");

class ProductSearcher {
	async ExecuteCommand(cmdData, acc, res) {
		var SellerLocation;
		const auth = getAuth();
		const user = auth.currentUser;
		if (user !== null) res.json({"status": 8});
		user.providerData.forEach((profile) => {
			SellerLocation = profile.sellerLoc;
		});

		const db = getFirestore();
		const productRef = db.collection("Product").doc(cmdData.productId);
		const doc = await productRef.get();
		if (!doc.exists) res.json({"status": 7});

	}
}

module.exports = ProductSearcher;
