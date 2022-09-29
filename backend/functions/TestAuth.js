class TestAuth {
	ExecuteCommand(cmdData, res) {
        const idToken = cmdData.authKey;

        getAuth()
        .verifyIdToken(idToken)
        .then((decodedToken) => {
            const uid = decodedToken.uid;
            res.json({
                "status": 0,
            });
        })
        .catch((error) => {
            res.json({
                "status": 1,
            });
        });
	}
}

module.exports = TestAuth;
