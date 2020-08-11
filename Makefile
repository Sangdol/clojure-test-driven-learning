test-watch:
	lein test-refresh :changes-only

test-js:
	lein doo chrome-headless test
