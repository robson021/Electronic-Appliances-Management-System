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

    this.checkIfAdmin = function () {
        $http.get(url + '/admin/am-i-admin/')
            .success(function (response) {
                if (response === 'OK') {
                    $rootScope.isAdmin = true;
                }
            });
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
                    toastr.error(
                        'Error. Invalid login or password.' +
                        'Or your account may not be activated yet.'
                    );
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
                self.displayMessage(response,
                    'Successfully deleted the building.',
                    'Could not delete the building.');
            });
    };

    this.renameBuilding = function (b, newName) {
        let uri = '/user-service/rename/building/' + b + '/' + newName + '/';
        $http.post(url + uri, null)
            .success(function (response) {
                self.displayMessage(response,
                    'Successfully renamed the building.',
                    'Could not rename the building.');
            });
    };

    this.deleteExistingRoom = function (id) {
        let uri = '/user-service/delete/room/' + id + '/';
        $http.delete(url + uri, null)
            .success(function (response) {
                self.displayMessage(response,
                    'Successfully deleted the room.',
                    'Could not delete the room.');
            });
    };

    this.renameRoom = function (id, newName) {
        let uri = '/user-service/rename/room/' + id + '/' + newName + '/';
        $http.post(url + uri, null)
            .success(function (response) {
                self.displayMessage(response,
                    'Successfully renamed the room.',
                    'Could not rename the room.');
            });
    };

    this.deleteExistingAppliance = function (id) {
        let uri = '/user-service/delete/appliance/' + id + '/';
        $http.delete(url + uri, null)
            .success(function (response) {
                self.displayMessage(response,
                    'Successfully renamed the appliance.',
                    'Could not rename the appliance.');
            });
    };

    this.renameAppliance = function (id, newName) {
        let uri = '/user-service/rename/appliance/' + id + '/' + newName + '/';
        $http.post(url + uri, null)
            .success(function (response) {
                self.displayMessage(response,
                    'Successfully deleted the appliance.',
                    'Could not delete the appliance.');
            });
    };

    this.displayMessage = function (response, okMsg, errorMsg) {
        if (response === 'OK') {
            toastr.info(okMsg);
        } else {
            toastr.error(errorMsg);
        }
    };

    this.clearObject = function (obj) {
        for (let member in obj) {
            delete obj[member];
        }
    };
});