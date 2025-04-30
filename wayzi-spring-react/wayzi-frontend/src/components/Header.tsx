import { Link } from 'react-router-dom';
import {useUser} from "../context/UserContext.tsx";
import '../styles/header.css'; // Adjust this path to your context
 // Adjust this path to your context

export const Header = () => {
    const { currentUser } = useUser();

    return (
        <nav className="navbar navbar-expand-lg" style={{ position: "relative", zIndex: 1 }}>
            <div className="container">
                <Link className="navbar-brand" to="/">
                    <img src="/assets/Logo_FINKI_UKIM_EN.jpg" alt="Wayzi Logo" height="60" />
                </Link>

                <button
                    className="navbar-toggler"
                    type="button"
                    data-bs-toggle="collapse"
                    data-bs-target="#navbarNav"
                    aria-controls="navbarNav"
                    aria-expanded="false"
                    aria-label="Toggle navigation"
                >
                    <span className="navbar-toggler-icon"></span>
                </button>

                <div className="collapse navbar-collapse" id="navbarNav">
                    <ul className="navbar-nav ms-auto mb-2 mb-lg-0 d-flex align-items-center">
                        <li className="nav-item">
                            <Link className="nav-link" to="/rides">
                                <i className="fa-solid fa-magnifying-glass"></i> Search
                            </Link>
                        </li>
                        <li className="nav-item">
                            <Link className="nav-link " to="/rides/publish">
                                <button className="btn btn-light p-2 rounded">
                                    <i className="fa-solid fa-plus"></i> <span className="d-3">Publish a ride</span>
                                </button>

                            </Link>
                        </li>

                        {/* Dropdown menu - Desktop only */}
                        <li className="nav-item dropdown d-none d-lg-block">
                            <a
                                className="nav-link dropdown-toggle"
                                href="#"
                                role="button"
                                data-bs-toggle="dropdown"
                                aria-expanded="false"
                            >
                                <img
                                    src='/assets/defualt-profile-pic.png'
                                    className="profile-pic"
                                    alt="ProfilePage"
                                    width="40"
                                    height="40"
                                />
                            </a>
                            <ul className="dropdown-menu dropdown-menu-dark dropdown-menu-end">
                                {currentUser ? (
                                    <>
                                        <li>
                                            <Link className="dropdown-item d-flex align-items-center gap-2 py-2" to="/profile">
                                                <i className="fa-solid fa-user"></i>
                                                <span>Profile</span>
                                            </Link>
                                        </li>
                                        <li>
                                            <Link className="dropdown-item d-flex align-items-center gap-2 py-2" to="/rides/published">
                                                <i className="fa-solid fa-signs-post"></i>
                                                <span>Published rides</span>
                                            </Link>
                                        </li>
                                        <li>
                                            <Link className="dropdown-item d-flex align-items-center gap-2 py-2" to="/rides/booked">
                                                <i className="fa-solid fa-car-side"></i>
                                                <span>Booked rides</span>
                                            </Link>
                                        </li>
                                        <li>
                                            <Link className="dropdown-item d-flex align-items-center gap-2 py-2" to="/payments">
                                                <i className="fa-solid fa-building-columns"></i>
                                                <span>Payments & refunds</span>
                                            </Link>
                                        </li>
                                        <li>
                                            <Link className="dropdown-item d-flex align-items-center gap-2 py-2" to="/logout">
                                                <i className="fa-solid fa-arrow-right-from-bracket"></i>
                                                <span>Log out</span>
                                            </Link>
                                        </li>
                                    </>
                                ) : (
                                    <>
                                        <li>
                                            <Link className="dropdown-item" to="/signin">
                                                Log in
                                            </Link>
                                        </li>
                                        <li>
                                            <Link className="dropdown-item" to="/signup">
                                                Create account
                                            </Link>
                                        </li>
                                    </>
                                )}
                            </ul>
                        </li>

                        {/* Mobile links */}
                        {currentUser ? (
                            <>
                                <li className="nav-item d-lg-none">
                                    <Link className="nav-link" to="/profile">
                                        Profile
                                    </Link>
                                </li>
                                <li className="nav-item d-lg-none">
                                    <Link className="nav-link" to="/payments">
                                        Payments & refunds
                                    </Link>
                                </li>
                                <li className="nav-item d-lg-none">
                                    <Link className="nav-link" to="/logout">
                                        Log out
                                    </Link>
                                </li>
                            </>
                        ) : (
                            <>
                                <li className="nav-item d-lg-none">
                                    <Link className="nav-link" to="/signin">
                                        Log in
                                    </Link>
                                </li>
                                <li className="nav-item d-lg-none">
                                    <Link className="nav-link" to="/signup">
                                        Create account
                                    </Link>
                                </li>
                            </>
                        )}
                    </ul>
                </div>
            </div>
        </nav>
    );
};

export default Header;
