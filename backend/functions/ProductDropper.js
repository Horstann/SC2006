const { getFirestore, Timestamp } = require('firebase-admin/firestore');
const { admin } = require('firebase-admin');

class ProductDropper {
	async ExecuteCommand(cmdData, acc, res) {
        if (
            cmdData.dropAll == undefined ||
            cmdData.productId == undefined ||
            cmdData.quantity == undefined
        ) {
            res.json({"status": 9});
            return;
        }

        if (acc == null) {
            res.json({"status": 6});
            return;
        }

		const db = getFirestore();
		const productRef = db.collection("Product").doc(cmdData.productId);
		const doc = await productRef.get();

		if (!doc.exists) {
            res.json({"status": 7});
            return;
        }
        
		if (doc.data().ClosingTime.toMillis() < Timestamp.now().toMillis()) {
            res.json({"status": 11});
            return;
        }

        let boughtProducts = acc.data().BoughtProducts;
        if (boughtProducts[doc.id] == undefined) {
            boughtProducts[doc.id] = 0;
        }

        if (cmdData.dropAll) {
            cmdData.quantity = boughtProducts[doc.id];
        }

        if (boughtProducts[doc.id] < cmdData.quantity) {
            res.json({"status": 13});
            return;
        }

        boughtProducts[doc.id] -= cmdData.quantity;

        if (boughtProducts[doc.id] <= 0){
            boughtProducts.delete(doc.id);
        }
        
        acc.ref.update({
            BoughtProducts: boughtProducts
        });

        productRef.update({
            TotalBought: doc.data().TotalBought - cmdData.quantity
        });
        
		res.json({"status": 0});
	}
}

module.exports = ProductDropper;