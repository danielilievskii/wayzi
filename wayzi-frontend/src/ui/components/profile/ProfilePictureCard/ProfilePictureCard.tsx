import {useDispatch, useSelector} from "react-redux";
import {AppDispatch, RootState} from "../../../../redux/store.ts";
import {useUser} from "../../../../context/UserContext.tsx";
import {useEffect, useRef, useState} from "react";
import {downloadProfilePic, submitProfilePic} from "../../../../redux/slices/profilePicSlice.ts";

export const ProfilePictureCard = () => {
    const dispatch = useDispatch<AppDispatch>();

    const {currentUser} = useUser();
    const currentUserId = String(currentUser?.id);

    const [file, setFile] = useState<File | null>(null);
    const fileInputRef = useRef<HTMLInputElement | null>(null);
    const [showBar, setShowBar] = useState(false);

    const {pictures} = useSelector((state: RootState) => state.profilePics);
    const [profilePic, setProfilePic] = useState(pictures[currentUserId] || "/assets/images/default-profile-pic.png");

    useEffect(() => {
        if (pictures[currentUserId]) {
            setProfilePic(pictures[currentUserId]);
        }
    }, [pictures, currentUserId]);

    const showPreview = (e: React.ChangeEvent<HTMLInputElement>) => {
        const selectedFile = e.target.files?.[0];
        if (selectedFile) {
            setFile(selectedFile);
            setShowBar(true);
            const reader = new FileReader();
            reader.onload = (ev) => {
                setProfilePic(ev.target?.result as string);
            };
            reader.readAsDataURL(selectedFile);
        }
    };

    const cancelUpload = () => {
        setFile(null);
        setShowBar(false);
        setProfilePic(pictures[currentUserId] || "/assets/default-profile-pic.png");
    };

    const onSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        if (!file) return;

        dispatch(submitProfilePic({profilePicFile: file, userId: currentUserId}))
            .unwrap()
            .then(({userId}) => {
                dispatch(downloadProfilePic(userId))
                    .then(() => {
                        setShowBar(false)
                    });
            })
            .catch((error) => {
                console.error('Failed to submit profile picture', error);
            });
    };

    return (
        <div className="floating-card shadow-sm px-4 py-3 container">
            <div className="d-flex justify-content-between align-items-end">
                <div className="d-flex align-items-center gap-3">
                    <img
                        id="profile-pic"
                        className="profile-pic"
                        src={profilePic}
                        width="100"
                        height="100"
                        alt=""
                    />
                    <div>
                        <h5 className="fw-bold text-dark-emphasis">{currentUser?.name}</h5>
                        <p style={{color: "gray"}}>21 years old</p>
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

            <form onSubmit={onSubmit}>
                <input
                    type="file"
                    name="file"
                    ref={fileInputRef}
                    accept="image/*"
                    style={{display: "none"}}
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
    )
}