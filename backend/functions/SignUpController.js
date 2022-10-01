const firestore = require("firebase-admin/firestore");

class SignUpController {
	async ExecuteCommand(cmdData, acc, res) {
		let authoriser = new Authoriser();

		// Get uid
		let uid = await authoriser.GetUidFromAuthKey(cmdData.newAuthKey);
		if (uid == null) {
			res.json({"status": 1});
			return;
		}

		// Check if account already exists
		let checkAcc = await authoriser.GetAccFromUid(uid);
		if (checkAcc != null) {
			res.json({"status": 8});
			return;
		}

		// TODO: Check if location is in Singapore.
		
		// Create account.
		let db = firestore.getFirestore();
		await db.collection("User").add({
			"HomeAddress": cmdData.homeAddr,
			"HomeLocation": cmdData.homeLoc,
			"UID": uid
		});
		res.json({"status": 0});
	}
}

module.exports = SignUpController;
