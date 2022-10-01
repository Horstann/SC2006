const { getFirestore } = require('firebase-admin/firestore');
const { getAuth } = require("firebase/auth");

class AddProduct {
	async ExecuteCommand(cmdData, res) {
		const auth = getAuth();
		const user = auth.currentUser;

		var SellerLocation;
		var SellerAddress;

		if (user !== null) {
			user.providerData.forEach((profile) => {
				SellerLocation = profile.sellerLoc;
				SellerAddress = profile.sellerAddr;
			});
		} else {
			res.json({"status": 8});
		}

		const data = {
			name: cmdData.name,
			totalUnits: cmdData.totalUnits,
			totalBought: 0,
			priceThresholds: cmdData.priceThresholds,
			unitThresholds: cmdData.unitThresholds,
			durationLeft: cmdData.durationLeft,
			desc: cmdData.desc,
			picUrl: cmdData.picUrl,
			sellerLoc: SellerLocation,
			sellerAddr: SellerAddress
		};

		const db = getFirestore();
		const result = await db.collection('Product').doc().set(data);
		res.json({"status": 0});
	}
}

module.exports = AddProduct;
