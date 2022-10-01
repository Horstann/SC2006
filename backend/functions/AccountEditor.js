const firestore = require("firebase-admin/firestore");

class AccountEditor {
	async ExecuteCommand(cmdData, acc, res) {
		if (cmdData.homeLoc == undefined ||
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

		await acc.set({
			"HomeAddress": cmdData.homeAddr,
			"HomeLocation": cmdData.homeLoc,
			"UID": acc.data().uid
		});
		
		res.json({"status": 0});
	}
}

module.exports = AccountEditor;
