var app = angular
    .module("ngApp", ['ngMaterial', 'ui.router', 'ngMessages'])
    .config(function ($stateProvider, $urlRouterProvider, $httpProvider) {


        // required for CORS
        $httpProvider.defaults.withCredentials = true;

        $stateProvider
            .state('default', {
                url: '/',
                templateUrl: '/partials/login.html',
                controller: 'login-ctrl'
            });

        $urlRouterProvider.otherwise("/");

    }); //end of config


// global data
angular.module('ngApp')
    .run(function ($rootScope) {
    });
