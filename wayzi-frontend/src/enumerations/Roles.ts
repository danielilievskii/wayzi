export const getRoleName = (role: string) => {
    switch (role) {
        case 'ROLE_ADMIN_USER':
            return 'ADMIN_USER';
        case 'ROLE_STANDARD_USER':
            return 'STANDARD_USER';
    }
}

export default {
    ADMIN_USER: 'ROLE_ADMIN_USER',
    STANDARD_USER: 'ROLE_STANDARD_USER',
    GUEST: 'ROLE_GUEST'
};