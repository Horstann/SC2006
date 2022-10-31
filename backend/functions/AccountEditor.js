const firestore = require("firebase-admin/firestore");

class AccountEditor {
	async ExecuteCommand(cmdData, acc, res) {
		// TODO: Check if location is in Singapore.
		// https://gis.stackexchange.com/questions/115301/how-to-find-a-location-is-inside-a-country

		if (acc == null) {
			res.json({"status": 6});
			return;
		}

		let db = firestore.getFirestore();
		await db.collection("User").doc(acc.id).set({
			HomeAddress: cmdData.homeAddr ?? acc.data().HomeAddress,
			HomeLocation: new firestore.GeoPoint(
				cmdData.homeLat ?? acc.data().HomeLocation.latitude,
				cmdData.homeLong ?? acc.data().HomeLocation.longitude),
			QRCode: cmdData.qrcode ?? acc.data().QRCode,
			UID: acc.data().UID
		});

		res.json({"status": 0});
	}
}

module.exports = AccountEditor;
