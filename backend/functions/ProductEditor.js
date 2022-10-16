const { getFirestore } = require('firebase-admin/firestore');

class ProductEditor {
	async ExecuteCommand(cmdData, acc, res) {
		const db = getFirestore();
        const productRef = db.collection("Product").doc(cmdData.productId);
        const doc = await productRef.get();
		if (!doc.exists) res.json({"status": 7});

        if (cmdData.name != null) await productRef.update({Name: cmdData.name});
        if (cmdData.totalUnits != null) await productRef.update({TotalUnits: cmdData.totalUnits});
        if (cmdData.priceThresholds != null) await productRef.update({PriceThresholds: cmdData.priceThresholds});
        if (cmdData.unitThresholds != null) await productRef.update({UnitThresholds: cmdData.unitThresholds});
        if (cmdData.closingTime != null){
            const [dateValues, timeValues] = (cmdData.closingTime+":00").split(' ');
            const [month, day, year] = dateValues.split('/');
            const [hours, minutes, seconds] = timeValues.split(':');
            const date = new Date(+year, month - 1, +day, +hours, +minutes, +seconds);
            const timestamp = date.fromDate();

            await productRef.update({ClosingTime: timestamp});
        }
        if (cmdData.desc != null) await productRef.update({Desc: cmdData.desc});
        if (cmdData.pics != null) await productRef.update({Pictures: cmdData.pics});

		res.json({
			"status": 0,
            "pictures": productRef.data().Pictures,
			"productId": cmdData.productId
		});
	}
}

module.exports = ProductEditor;