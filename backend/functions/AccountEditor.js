const firestore = require("firebase-admin/firestore");

class AccountEditor {
	async ExecuteCommand(cmdData, acc, res) {
		if (cmdData.homeLat == undefined ||
				cmdData.homeLong == undefined ||
				cmdData.homeAddr == undefined) {
			res.json({"status": 9});
			return;
		}

		// TODO: Check if location is in Singapore.
		// https://gis.stackexchange.com/questions/115301/how-to-find-a-location-is-inside-a-country

		if (acc == null) {
			res.json({"status": 6});
			return;
		}

		if (cmdData.deleteAccount != null){
			if (cmdData.deleteAccount == true){
				let accs = await db.collection("User").where("UID", "==", acc.data().uid).get();
				await db.collection('Users').doc(accs.docs[0].id).delete();
				res.json({"status": 0});
			}
		}

		const result = await acc.set({
			HomeAddress: cmdData.homeAddr,
			HomeLocation: new firestore.GeoPoint(
				cmdData.homeLat, cmdData.homeLong),
			UID: acc.data().uid
		});
		
		if (cmdData.qrcode != null){
			await acc.set({
				QRCode: cmdData.qrcode
			}, { merge: true });
		}

		res.json({"status": 0});
	}
}

module.exports = AccountEditor;
