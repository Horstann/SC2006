const { getFirestore } = require('firebase-admin/firestore');

class ProductAdder {
	async ExecuteCommand(cmdData, acc, res) {
		const db = getFirestore();

		const data = {
			Name: cmdData.name,
			TotalUnits: cmdData.totalUnits,
			TotalBought: 0,
			PriceThresholds: cmdData.priceThresholds,
			UnitThresholds: cmdData.unitThresholds,
			ClosingTime: cmdData.closingTime,
			Desc: cmdData.desc,
			Pictures: cmdData.pics,
			Seller: acc
		};

		const result = await db.collection('Product').add(data);
		res.json({
			"status": 0,
			"productId": result.id
		});
	}
}

module.exports = ProductAdder;