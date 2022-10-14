const firestore = require("firebase-admin/firestore");

class SignUpController {
	async ExecuteCommand(cmdData, acc, res) {
		if (cmdData.newAuthKey == undefined ||
				cmdData.homeLat == undefined ||
				cmdData.homeLong == undefined ||
				cmdData.homeAddr == undefined) {
			res.json({"status": 9});
			return;
		}

		// Get uid
		let uid = await global.authoriser.GetUidFromAuthKey(cmdData.newAuthKey);
		if (uid == null) {
			res.json({"status": 1});
			return;
		}

		// Check if account already exists
		let checkAcc = await global.authoriser.GetAccFromUid(uid);
		if (checkAcc != null) {
			res.json({"status": 8});
			return;
		}

		// TODO: Check if location is in Singapore.
		// https://gis.stackexchange.com/questions/115301/how-to-find-a-location-is-inside-a-country
		
		// Create account.
		let db = firestore.getFirestore();
		await db.collection("User").add({
			"HomeAddress": cmdData.homeAddr,
			"HomeLocation": new firestore.GeoPoint(
				cmdData.homeLat, cmdData.homeLong),
			"UID": uid
		});
		res.json({"status": 0});
	}
}

module.exports = SignUpController;
