app.service('httpSvc', function ($rootScope, $http, $state) {

    // to allow CORS run: chromium-browser --disable-web-security --user-data-dir

    const self = this;
    const url = "http://localhost:8080";

    // ----- basic user actions (login/register/logout etc...) -------

    this.checkIfLoggedIn = function () {
        if (!$rootScope.loggedIn) {
            $state.go('login-register');
            toastr.info('You must login first');
        }
    };

    this.loginUser = function (user) {
        let uri = '/login/' + user.email + '/' + user.password + '/';
        $http.post(url + uri, null)
            .success(function (response) {
                console.info('login user:' + response);
                if (response === 'OK') {
                    $rootScope.loggedIn = true;
                    $state.go('default');
                    toastr.success('You have been logged in.');
                    self.clearObject(user);
                } else {
                    toastr.error('Error. Invalid login or password.')
                }
            });
    };

    this.registerNewUser = function (user) {
        if (user.password !== user.repassword) {
            toastr.info('Passwords do not match!');
            return;
        }
        let uri = '/register/' + user.email + '/' + user.password + '/' + user.name + '/' + user.surname + '/';
        $http.put(url + uri, null)
            .success(function (response) {
                console.info('register new user: ' + response);
                if (response === 'OK') {
                    $state.go('default');
                    toastr.success('Successfully registered your account.');
                    //self.loginUser(user);
                } else if (response === 'FORBIDDEN') {
                    toastr.error('Invalid e-mail or password pattern.');
                } else {
                    toastr.error('Error. Could not register the user.');
                }
            });
    };

    this.logoutUser = function () {
        $rootScope.loggedIn = false;
        $http.post(url + '/logout/', null);
        $state.go('default');
    };

    // ------- get things ----------

    this.getAllBuildings = function () {
        let uri = '/user-service/get-all-buildings/';
        return $http.get(url + uri, null);
    };

    this.getAllRoomsInBuilding = function (buildingNumber) {
        let uri = '/user-service/' + buildingNumber + '/';
        return $http.get(url + uri, null);
    };

    this.getAllAppliancesInRoom = function (roomId) {
        let uri = '/user-service/get-all-appliances/' + roomId + '/';
        return $http.get(url + uri, null);
    };

    // ---------- edit buttons -------------

    this.deleteExistingBuilding = function (b) {
        let uri = '/user-service/delete/building/' + b + '/';
        $http.delete(url + uri, null)
            .success(function (response) {
                if (response === 'OK') {
                    toastr.info('Successfully deleted the building.');
                } else {
                    toastr.error('Could not delete the building.')
                }
            });
    };

    this.renameBuilding = function (b, newName) {
        let uri = '/user-service/rename/building/' + b + '/' + newName + '/';
        $http.post(url + uri, null)
            .success(function (response) {
                if (response === 'OK') {
                    toastr.info('Successfully renamed the building.');
                } else {
                    toastr.error('Could not rename the building.')
                }
            });
    };

    this.clearObject = function (obj) {
        for (let member in obj) {
            delete obj[member];
        }
    };
});