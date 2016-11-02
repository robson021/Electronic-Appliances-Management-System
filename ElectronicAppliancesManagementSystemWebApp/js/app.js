var app = angular
    .module("ngApp", ['ui.router'])
    .config(function ($stateProvider, $urlRouterProvider, $httpProvider) {

        // required for CORS session
        $httpProvider.defaults.withCredentials = true;

        $stateProvider
            .state('default', {
                url: '/',
                templateUrl: 'partials/main-view.html'
            })
            .state('login-register', {
                url: '/login-register',
                templateUrl: 'partials/login-register.html',
                controller: 'login-register-ctrl'
            })
            .state('about', {
                url: '/about',
                templateUrl: 'partials/about.html'
            });

        $urlRouterProvider.otherwise("/");

    }); //end of config


// global data
angular.module('ngApp')
    .run(function ($rootScope, $http) {
        $rootScope.loggedIn = false;
        $http.get('/js/ServerAddress.json')
            .then(function (res) {
                $rootScope.serverAddress = res.data.address;
                console.info("back-end address: " + $rootScope.serverAddress);
            });
    });
