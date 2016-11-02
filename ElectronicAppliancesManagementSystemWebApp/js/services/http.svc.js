app.service('httpSvc', function ($rootScope, $http, $state) {

    const url = $rootScope.serverAddress;
    const self = this;

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
                    toastr.success('Successful registered your account.');
                    self.loginUser(user);
                } else if (response === 'FORBIDDEN') {
                    toastr.error('Invalid e-mail or password pattern.');
                } else {
                    toastr.error('Error. Could not register the user.');
                }
            });
    };

    this.getAllBuildings = function () {
        let uri = '/user-service/get-all-buildings/';
        $http.get(url + uri, null)
            .success(function (response) {
                console.info('get all buildings: ' + response);
            });
    };

    this.clearObject = function (obj) {
        for (let member in obj) {
            delete obj[member];
        }
    };
});