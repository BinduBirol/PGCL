
function isPositiveNumber(n) { 
	if(n<0) return false;
    return !isNaN(parseFloat(n)) && isFinite(n); 
}
function isPositiveInteger(n) {
    return 0 === n % (!isNaN(parseFloat(n)) && 0 <= ~~n);
}