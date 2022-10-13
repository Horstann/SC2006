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
			/* timestamp must be in the format below
			{
				"_seconds": 1669946400,
				"_nanoseconds": 438000000
			}
			*/
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

/*
{
    "cmd": "addProduct",
    "authKey": "",
    "name": "A Cow",
    "totalUnits": 10,
    "priceThresholds": [100, 85, 75, 65, 50],
    "unitThresholds": [0, 2, 4, 6, 10],
    "desc": "Yep, a cow",
    "closingTime": {
				"_seconds": 1669946400,
				"_nanoseconds": 438000000
		},
    "pics": ["https://img.fruugo.com/product/0/70/7700700_max.jpg", "https://upload.wikimedia.org/wikipedia/commons/0/0c/Cow_female_black_white.jpg"]
}
*/