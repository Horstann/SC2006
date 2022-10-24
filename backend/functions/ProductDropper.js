const { getFirestore, Timestamp } = require('firebase-admin/firestore');
const { admin } = require('firebase-admin');

class ProductDropper {
	async ExecuteCommand(cmdData, acc, res) {
		const db = getFirestore();
		const productRef = db.collection("Product").doc(cmdData.productId);
		const doc = await productRef.get();

		if (!doc.exists) res.json({"status": 7});
		if (doc.data().closingTime > Timestamp.now()) res.json({"status": 11});
        if (!(doc.id in acc.BoughtProducts)) res.json({"status": 14});

        let newBoughtProducts = doc.data().BoughtProducts;
        if (doc.data().dropAll != null){
            if (doc.data().dropAll == true) {
                delete newBoughtProducts[doc.id];
                await acc.set({
                    BoughtProducts: newBoughtProducts
                });
            }
        }
        if (cmdData.quantity > (acc.BoughtProducts[doc.id]) || cmdData.quantity < 1) res.json({"status": 13});

		let newTotalBought = doc.data().TotalBought - cmdData.quantity;
		await doc.set({
				TotalBought: newTotalBought
		});

        let newQuantity = acc.BoughtProducts[doc.id] - cmdData.quantity;

        if (newQuantity == 0){
            delete newBoughtProducts[doc.id];
            await acc.set({
                BoughtProducts: newBoughtProducts
            });
        }
        else{
            newBoughtProducts[doc.id] = newQuantity;
            await acc.set({
                BoughtProducts: newBoughtProducts
            });
        }        
		
		res.json({"status": 7});
	}
}

module.exports = ProductDropper;