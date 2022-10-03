const { getFirestore } = require('firebase-admin/firestore');

class ProductAdder {
	async ExecuteCommand(cmdData, acc, res) {
		const data = {
			name: cmdData.name,
			totalUnits: cmdData.totalUnits,
			totalBought: 0,
			priceThresholds: cmdData.priceThresholds,
			unitThresholds: cmdData.unitThresholds,
			closingTime: cmdData.closingTime,
			desc: cmdData.desc,
			picUrl: cmdData.picUrl,
			sellerLoc: acc.HomeLocation,
			sellerAddr: acc.HomeAddress
		};

		const db = getFirestore();
		const result = await db.collection('Products').add(data);
		res.json({
			"status": 0,
			"productId": result.id
		});
	}
}

module.exports = ProductAdder;
