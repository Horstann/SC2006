const { getFirestore, Timestamp } = require('firebase-admin/firestore');
const ImageUploader = require("./ImageUploader.js");

class ProductEditor {
	async ExecuteCommand(cmdData, acc, res) {
        if (cmdData.productId == undefined) {
            res.json({"status": 9});
            return;
        }

		const db = getFirestore();
        const productRef = db.collection("Product").doc(cmdData.productId);
        const doc = await productRef.get();

		if (!doc.exists) {
            res.json({"status": 7});
            return;
        }

        if (doc.data().Seller.id != acc.id) {
            res.json({"status": 6});
            return;
        }

        if (cmdData.name != undefined) await productRef.update({Name: cmdData.name});
        if (cmdData.totalUnits != undefined) await productRef.update({TotalUnits: cmdData.totalUnits});
        if (cmdData.priceThresholds != undefined) await productRef.update({PriceThresholds: cmdData.priceThresholds});
        if (cmdData.unitThresholds != undefined) await productRef.update({UnitThresholds: cmdData.unitThresholds});
        if (cmdData.closingTime != undefined){
            const [dateValues, timeValues] = (cmdData.closingTime+":00").split(' ');
            const [month, day, year] = dateValues.split('/');
            const [hours, minutes, seconds] = timeValues.split(':');
            const date = new Date(+year, month - 1, +day, +hours, +minutes, +seconds);
            const timestamp = Timestamp.fromDate(date);

            await productRef.update({ClosingTime: timestamp});
        }
        if (cmdData.desc != undefined) await productRef.update({Description: cmdData.desc});
        if (cmdData.pics != undefined) {
            await productRef.update({Pictures: cmdData.pics});
        }

		res.json({
			"status": 0,
		});
	}
}

module.exports = ProductEditor;