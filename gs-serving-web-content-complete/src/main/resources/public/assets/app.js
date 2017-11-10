var app = angular.module('angular', []);
app.service('ProductsService', function($http) {
	var urlBase = 'api/products';

	this.getProducts = function(query) {
		return $http({
			url : urlBase,
			params : {
				query : query
			},
			method : "GET",
		});
	};
	
});



app.controller('ProductController', function CartController($scope,
		ProductsService) {
	$scope.products = []

	ProductsService.getProducts().then(function(response) {
		$scope.products = response.data;
	})
	
	$scope.search=function(){
		ProductsService.getProducts($scope.query).then(function(response) {
			$scope.products = response.data;
		})
	}

});