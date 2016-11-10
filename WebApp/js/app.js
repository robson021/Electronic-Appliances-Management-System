var app = angular
    .module("ngApp", ['ui.router', 'ngDialog'])
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
            .state('user-panel', {
                url: '/user-panel',
                templateUrl: 'partials/user-panel.html',
                controller: 'user-panel-ctrl'
            })
            .state('admin-panel', {
                url: '/admin-panel',
                templateUrl: 'partials/admin-panel.html',
                controller: 'admin-panel-ctrl'
            })
            .state('guest', {
                url: '/guest',
                templateUrl: 'partials/guest-panel.html',
                controller: 'guest-panel-ctrl'
            })
            .state('about', {
                url: '/about',
                templateUrl: 'partials/about.html'
            });

        $urlRouterProvider.otherwise("/");

    }); //end of config


// global data
angular.module('ngApp')
    .run(function ($rootScope) {
        $rootScope.loggedIn = false;
        $rootScope.isAdmin = false;
    });
