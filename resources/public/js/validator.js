$(document).ready(function() {
    $('#registrationForm').bootstrapValidator({
        fields: {
            fullname: {
                validators: {
                    notEmpty: {
                        message: 'Please provide your full name'
                    }
                }
            },
            username: {
                validators: {
                    notEmtpy: {
                        message: 'Please provide your email address'
                    },
                    emailAddress: {
                        message: 'The email you provided is not valid'
                    }
                }
            },
            phone-number: {
                validators: {
                    phone: {
                        message: 'Please enter a valid phone number'
                    }
                }
            },
            password: {
                validators: {
                    notEmpty: {
                        message: 'Please provide a password'
                    },
                    stringLength {
                        min: 8,
                        message: 'The password must be at least 8 characters'
                    }
                }
            },
            password-confirm: {
                validators: {
                    notEmpty: {
                        message: 'Please confirm your password'
                    },
                    identical: {
                        field: 'password',
                        message: 'Passwords must be identiical'
                    }
                }
            }
        }
    });
});
