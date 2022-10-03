const { getFirestore } = require('firebase-admin/firestore');

class ProductAdder {
	async ExecuteCommand(cmdData, acc, res) {
		const data = {
			Name: cmdData.name,
			TotalUnits: cmdData.totalUnits,
			TotalBought: 0,
			PriceThresholds: cmdData.priceThresholds,
			UnitThresholds: cmdData.unitThresholds,
			ClosingTime: cmdData.closingTime,
			Desc: cmdData.desc,
			PicUrl: cmdData.picUrl,
			SellerLoc: acc.HomeLocation,
			SellerAddr: acc.HomeAddress
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
