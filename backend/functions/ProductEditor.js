const { getFirestore } = require('firebase-admin/firestore');

class ProductEditor {
	async ExecuteCommand(cmdData, acc, res) {
		const db = getFirestore();
        const productRef = db.collection("Product").doc(cmdData.productId);
        const doc = await productRef.get();
		if (!doc.exists) res.json({"status": 7});

        const result = await productRef.update({
            Name: cmdData.name,
			TotalUnits: cmdData.totalUnits,
			PriceThresholds: cmdData.priceThresholds,
			UnitThresholds: cmdData.unitThresholds,
			ClosingTime: cmdData.closingTime,
			/* timestamp must be in the format below
			{
				"_seconds": 1669946400,
				"_nanoseconds": 438000000
			}
			*/
			Desc: cmdData.desc,
			Pictures: cmdData.pics
        });

		res.json({
			"status": 0,
			"productId": result.id
		});
	}
}

module.exports = ProductEditor;