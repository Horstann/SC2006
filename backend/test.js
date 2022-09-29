const { initializeApp, applicationDefault, cert } = require('firebase-admin/app');
const { getFirestore, Timestamp, FieldValue } = require('firebase-admin/firestore');

initializeApp();

const db = getFirestore();

var products = db.collection('ProductManager').get().then((snapshot) => {
    console.log(snapshot.docs);
    console.log(doc.data());
});