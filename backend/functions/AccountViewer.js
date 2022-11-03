class AccountViewer {
	ExecuteCommand(cmdData, acc, res) {
		if (acc == null) {
			res.json({
				"status": 6
			});
			return;
		}

		res.json({
			"status": 0,
			"homeLat": acc.data().HomeLocation.latitude,
			"homeLong": acc.data().HomeLocation.longitude,
			"homeAddr": acc.data().HomeAddress,
			"qrcode": acc.data().QRCode
		});
	}
}

module.exports = AccountViewer;
