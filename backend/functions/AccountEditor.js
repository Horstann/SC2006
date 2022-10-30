const firestore = require("firebase-admin/firestore");

class AccountEditor {
	async ExecuteCommand(cmdData, acc, res) {
		if (cmdData.homeLat == undefined ||
				cmdData.homeLong == undefined ||
				cmdData.homeAddr == undefined ||
				cmdData.qrcode == undefined) {
			res.json({"status": 9});
			return;
		}

		// TODO: Check if location is in Singapore.
		// https://gis.stackexchange.com/questions/115301/how-to-find-a-location-is-inside-a-country

		if (acc == null) {
			res.json({"status": 6});
			return;
		}

		let db = firestore.getFirestore();
		await db.collection("User").doc(acc.id).set({
			HomeAddress: cmdData.homeAddr,
			HomeLocation: new firestore.GeoPoint(
				cmdData.homeLat, cmdData.homeLong),
			UID: acc.data().UID,
			QRCode: cmdData.qrcode
		});

		res.json({"status": 0});
	}
}

module.exports = AccountEditor;
