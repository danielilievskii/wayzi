import './Header.css';
import { Link } from 'react-router-dom';
import {useUser} from "../../../../context/UserContext.tsx";
import {useSelector} from "react-redux";
import {RootState} from "../../../../redux/store.ts";
import {useEffect, useState} from "react";

export const Header = () => {
    const { currentUser, signOut } = useUser();
    const currentUserId = String(currentUser?.id);

    const { pictures } = useSelector((state: RootState) => state.profilePics);
    const [profilePic, setProfilePic] = useState(pictures[currentUserId] || "/assets/images/default-profile-pic.png");

    useEffect(() => {
        if (pictures[currentUserId]) {
            setProfilePic(pictures[currentUserId]);
        }
    }, [pictures, currentUserId]);

    const onSignOut = () => {
        signOut()
    }

    return (
        <nav className="fixed-top navbar navbar-expand-lg" style={{ zIndex: 1 }}>
            <div className="container">
                <Link className="navbar-brand" to="/">
                    <img src="/assets/images/Logo_FINKI_UKIM_EN.jpg" alt="Wayzi Logo" height="60" />
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
                                <button className="btn btn-light p-2 rounded">
                                    <i className="fa-solid fa-magnifying-glass"></i> Search
                                </button>

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
                                    src={profilePic}
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
                                            <Link className="dropdown-item d-flex align-items-center gap-2 py-2" to="/rides/bookings">
                                                <i className="fa-solid fa-suitcase-rolling"></i>
                                                <span>Bookings</span>
                                            </Link>
                                        </li>
                                        <li>
                                            <Link className="dropdown-item d-flex align-items-center gap-2 py-2" to="/payments">
                                                <i className="fa-solid fa-building-columns"></i>
                                                <span>Payments & refunds</span>
                                            </Link>
                                        </li>
                                        <li>
                                            <Link className="dropdown-item d-flex align-items-center gap-2 py-2" onClick={onSignOut}>
                                                <i className="fa-solid fa-arrow-right-from-bracket"></i>
                                                <span>Log out</span>
                                            </Link>
                                        </li>
                                    </>
                                ) : (
                                    <>
                                        <li>
                                            <Link className="dropdown-item" to="/login">
                                                Log in
                                            </Link>
                                        </li>
                                        <li>
                                            <Link className="dropdown-item" to="/register">
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
                                    <Link className="nav-link" to="/login">
                                        Log in
                                    </Link>
                                </li>
                                <li className="nav-item d-lg-none">
                                    <Link className="nav-link" to="/register">
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
