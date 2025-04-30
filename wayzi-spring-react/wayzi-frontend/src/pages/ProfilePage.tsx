import { useUser } from "../context/UserContext.tsx";
import {useState, useRef, useEffect} from "react";
import "../styles/home.css";
import '../styles/profile.css'
import {useDispatch, useSelector} from "react-redux";
import {AppDispatch, RootState} from "../redux/store.ts";
import {ProfileVehiclesSection} from "../components/profile/ProfileVehiclesSection.tsx";

export const ProfilePage = () => {
    const { currentUser } = useUser();

    const [previewPic, setPreviewPic] = useState(currentUser?.profilePicPath || "/assets/defualt-profile-pic.png");
    const [file, setFile] = useState<File | null>(null);
    const fileInputRef = useRef<HTMLInputElement | null>(null);
    const [showBar, setShowBar] = useState(false);

    const showPreview = (e: React.ChangeEvent<HTMLInputElement>) => {
        const selectedFile = e.target.files?.[0];
        if (selectedFile) {
            setFile(selectedFile);
            setShowBar(true);
            const reader = new FileReader();
            reader.onload = (ev) => {
                setPreviewPic(ev.target?.result as string);
            };
            reader.readAsDataURL(selectedFile);
        }
    };

    const cancelUpload = () => {
        setFile(null);
        setShowBar(false);
        setPreviewPic(currentUser?.profilePicPath || "/assets/defualt-profile-pic.png");
    };

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        if (!file) return;

        const formData = new FormData();
        formData.append("file", file);

        fetch("/profile/upload-profile-pic", {
            method: "POST",
            body: formData,
        }).then(() => {
            setShowBar(false);
            // Optionally update the user in context or refetch
        });
    };

    return (
        <div className="page-wrapper">
            <section className="custom-container mt-5">
                <div className="d-flex flex-column gap-3">
                    <div className="floating-card shadow-sm px-4 py-3 container">
                        <div className="d-flex justify-content-between align-items-end">
                            <div className="d-flex align-items-center gap-3">
                                <img
                                    id="profile-pic"
                                    className="profile-pic"
                                    src={previewPic}
                                    width="100"
                                    height="100"
                                    alt=""
                                />
                                <div>
                                    <h5 className="fw-bold text-dark-emphasis">
                                        {currentUser?.name}
                                    </h5>
                                    <p style={{ color: "gray" }}>21 years old</p>
                                </div>
                            </div>

                            <button
                                type="button"
                                className="btn btn-light"
                                onClick={() => fileInputRef.current?.click()}
                            >
                                <i className="fa-solid fa-camera"></i> Change profile picture
                            </button>
                        </div>

                        <form onSubmit={handleSubmit}>
                            <input
                                type="file"
                                name="file"
                                ref={fileInputRef}
                                accept="image/*"
                                style={{ display: "none" }}
                                onChange={showPreview}
                            />

                            {showBar && (
                                <div className="confirmation-bar">
                                    <div className="confirmation-bar-buttons">
                                        <button type="button" className="cancel-changes" onClick={cancelUpload}>
                                            Cancel
                                        </button>
                                        <button type="submit" className="save-changes">
                                            Save changes
                                        </button>
                                    </div>
                                </div>
                            )}
                        </form>
                    </div>

                    <div className="floating-card shadow-sm p-3">
                        <h5>Verify your profile</h5>
                    </div>

                    <div className="floating-card shadow-sm p-3">
                        <h5>About you</h5>
                    </div>

                    <ProfileVehiclesSection />


                </div>
            </section>
        </div>

    );
};
