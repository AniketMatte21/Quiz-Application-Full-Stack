//rules & conditions
document.querySelector(".start-exp-btn").addEventListener("click",(e)=>
{
    Swal.fire({
        title: '📋 Quiz Rules',
        html: `
    ⏱️ <b style="color: #1d1d1f">Time Limit:</b> 10 minutes<br><br>
    ✅ <b style="color: #1d1d1f">All 10 questions must be answered</b> to submit<br><br>
    🕒 <b style="color: #1d1d1f" >Auto-submit</b> only if all questions are answered before time runs out<br><br>
    ❌ <b style="color: #1d1d1f">Incomplete quizzes won’t be submitted</b><br><br>
    🧠 <b style="color: #1d1d1f">Each question = 1 mark</b><br><br>
    📊 <b style="color: #1d1d1f">Score shown after submission</b><br><br>
    🔍 <b style="color: #1d1d1f">See your wrong answers</b> after submitting<br><br>
    ⚠️ <b style="color: #1d1d1f">Don't refresh or leave the page</b><br>
  `,
        confirmButtonText: 'Start Quiz',
        icon: 'question'
    });


    // alert("clicked");
})
document.addEventListener("DOMContentLoaded",async (e)=>{

    const overlay = document.getElementById("overlay");
    const navLoginBtn=document.querySelector(".nav-login");
    const authLogin=document.querySelector(".auth-login");
    const mode=document.querySelector(".mode");
    const mainSection=document.querySelector(".main-section");
    const sideBar=document.querySelector(".side-bar");
    const fontColor=document.querySelectorAll(".font-color");
    const welcomeSection=document.querySelector(".welcome-section");
    const strExpBtn=document.querySelector(".start-exp-btn");
    const navSignup=document.querySelector(".nav-signup");
    const quizContainer=document.querySelector(".quiz-container");
    const userName=document.querySelector(".username");



    //Check if user is login or not
        try {
            await fetch("http://localhost:8080/check/isLogin")
                .then(res => res.json())
                .then((data) => {
                    localStorage.setItem("isLoggedIn", data.isLoggedIn);
                    if (data.isLoggedIn) {
                        navLoginBtn.classList.add("display");
                        navSignup.classList.add("display");

                        // creating logout btn
                        const logoutB = document.createElement("button");
                        logoutB.className = "logout-btn";
                        logoutB.innerHTML = `
                  
                        <p>logout</p>`
                        document.querySelector(".logout-handle").appendChild(logoutB);

                        //hiding native btns
                        document.querySelector(".native").classList.add("display");

                        //creating native logout
                        const nativeLogoutDiv=document.createElement("div");
                        nativeLogoutDiv.className="native-logout";
                        nativeLogoutDiv.innerHTML=`<form method="post" action="http://localhost:8080/doLogout">
                        <button class="native-logoutBtn" type="submit">logout</button>
                        </form>`

                        document.querySelector(".main-section").insertBefore(nativeLogoutDiv,document.querySelector(".welcome-section"));


                        //User Profile Pic
                        let username = data.username;
                        let firstLetter = username.charAt(0);

                        //creating user profile
                        const userProf = document.createElement("div");
                        userProf.className = "user-profile";
                        userProf.id = "profile";
                        userProf.innerHTML = `
                        <div  class="profile-pic">
                        ${firstLetter.toUpperCase()}
                        </div>
                        <div class="p-name">
                        <h3 class="user-info font-color username">${data.username}</h3>
                        <h5 class="user-info font-color" style="color: #1e3a8a">Quiz Master</h5>
                        </div>`
                        document.querySelector(".side-bar").insertBefore(userProf, document.querySelector(".notes"));

                    } else {
                        // document.querySelector(".side-bar").classList.add("display");
                        document.querySelector(".top-performer").classList.add("display");
                        //unlock-sidebar
                        const lock = document.createElement("div");
                        lock.className = "lock";
                        lock.innerHTML = `
                     <img src="/images/unlock_12266395.png" height="150" width="150" alt="lock"/>
                    <p style="color: black">Login to Unlock</p>`
                        document.querySelector(".side-bar").appendChild(lock);
                    }
                })

        } catch (error) {
            throw new Error(error.message)
        }
        ////////////////////////////////////////////////////////////////////////////////////////


        if (navLoginBtn != null) {
            navLoginBtn.addEventListener("click", () => {

                localStorage.setItem("navLogin", "login");
            })
        }

        if (navSignup !== null) {
            navSignup.addEventListener("click", () => {
                localStorage.setItem("navSignup", "signup");
            })
        }


        if (authLogin !== null) {
            authLogin.addEventListener("click", () => {
                localStorage.setItem("authLogin", "login");
            })
        }

        // Creating the quiz cards

        try {
            let response = await fetch("http://localhost:8080/quiz/getQuizes")
            if (!response.ok) {
                throw new Error("failed to fetch");
            }
            let data = await response.json();

            data.forEach((quiz) => {
                const card = document.createElement("div");
                card.className = "quiz-card";
                card.setAttribute("data-cardid", quiz.id);

                card.innerHTML = `
                <div class="card">
                <h2 style="color: black">🧠 ${quiz.quizType}</h2>
                <img src="${quiz.imgUrl}" alt="${quiz.quizType} quiz" height="120" width="235">
                 <p>${quiz.quizDescription}</p>
                <div class="info">
                <span class="badge">Type: ${quiz.quizType}</span>
                <span class="price">₹${quiz.price}</span>
                </div>
                <button class="btn" value="${quiz.quizType}" data-amount="${quiz.price}" data-cardid="${quiz.id}">Buy Now</button>
                <button class="start-quiz-btn display" data-cardid="${quiz.id}" value="${quiz.quizType}">Start Quiz</button>
                </div>
        `;

                quizContainer.appendChild(card);

                const buyNowBtn = document.querySelectorAll(".btn");

                buyNowBtn.forEach((buyNow) => {
                    buyNow.addEventListener("click", async () => {
                        let isLogin=localStorage.getItem("isLoggedIn");
                    if(isLogin==="false")
                        {
                            window.location.href="/authentication/auth.html?mode=login"
                            return;
                        }
                        const quizType = buyNow.value;

                        cardId = buyNow.dataset.cardid;
                        localStorage.setItem("cardId", cardId);


                        const curr = "inr";
                        const amt = parseInt(buyNow.dataset.amount);


                        let obj = {
                            name: quizType,
                            currency: curr,
                            amount: amt
                        }


                        try {
                            let response = await fetch("http://localhost:8080/payment/create-order", {
                                method: "POST",
                                headers: {
                                    "Content-Type": "application/json"
                                },
                                body: JSON.stringify(obj)
                            })

                            if (!response.ok) {
                                throw new Error("order not created")
                            } else {
                                response = await response.json();
                                console.log(response);
                                window.location.href = response.sessionUrl;

                            }

                        } catch (error) {
                            throw new Error(error.message);
                        }

                    })
                })


            })
        } catch (error) {
            console.log("something went wrong");
        }

        const urlParams = new URLSearchParams(window.location.search);
        const sessionId = urlParams.get("session_id");

        if (sessionId !== null) {
            const paymentStatus = urlParams.get("paymentStatus")
            console.log(paymentStatus)
            cardId = localStorage.getItem("cardId");
            console.log(cardId);

            let obj = {
                paymentStatus: paymentStatus,
                quizCardId: cardId
            }

            try {
                let response = await fetch(`http://localhost:8080/payment/checkSessionId?sessionId=${sessionId}`, {
                    method: "post",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    body: JSON.stringify(obj)
                })

                if (response.ok) {
                    let data = await response.text();
                    Swal.fire({
                        text:data,
                        position: 'top',
                        icon: 'success',
                        confirmButtonText: 'OK'
                    }).then(()=>{window.location.href = "index.html"})

                }




            } catch (error) {
                throw new Error(error.message);
            }

        }


        if (localStorage.getItem("isLoggedIn")) {
            try {
                await fetch("http://localhost:8080/quizPurchase/purchase")
                    .then(res => res.json())
                    .then(data => {
                        data.forEach((e) => {
                                const quizCard = document.querySelectorAll(".quiz-card");
                                quizCard.forEach((card) => {
                                    if (parseInt(e) === parseInt(card.dataset.cardid)) {
                                        const startQuizBtn = card.querySelectorAll(".start-quiz-btn");
                                        startQuizBtn.forEach((startQuiz) => {
                                            startQuiz.classList.remove("display")

                                        })

                                        const buyNowBtn = card.querySelectorAll(".btn");
                                        buyNowBtn.forEach((btn) => {
                                            btn.classList.add("display");
                                        })


                                    }
                                })
                            }
                        )
                    })
            } catch (error) {
                throw new Error(error.message);
            }
        }


        // fetching quiz questions by type
        const startQuizBtn = document.querySelectorAll(".start-quiz-btn");
        startQuizBtn.forEach((btn) => {
            btn.addEventListener("click", () => {
                const quizType = btn.value;
                localStorage.setItem("quizType", quizType);
                localStorage.setItem("quizId", btn.dataset.cardid)
                window.location.href = `/Quiz-questions-page/quiz.html?type=${quizType.toLowerCase()}`;

            })
        })

        // Notes
        if (localStorage.getItem("isLoggedIn")) {
            try {
                await fetch("http://localhost:8080/file/getFiles")
                    .then((res) => res.json())
                    .then((data) => {
                        const notes = document.createElement("div");
                        notes.className = "notes";
                        notes.innerHTML = ` <div class="notes-heading">
                    <h3 class="font-color">Notes</h3>
                    </div>
                    <div class="notes-section">
                    </div>`
                        document.querySelector(".side-bar").appendChild(notes);

                        data.forEach((e) => {
                            const ul = document.createElement("ul");
                            ul.className = "file-list";

                            ul.innerHTML = `
                        <li data-fileid=${e.fileId} data-filename=${e.name} class="file-item">${e.name}</li>`

                            document.querySelector(".notes-section").appendChild(ul);
                        })
                    })
            } catch (error) {
                throw new Error(error.message);
            }
        }


        //Accessing the notes
        document.querySelectorAll(".file-item").forEach((item) => {
            item.addEventListener("click", async () => {
                try {
                    let response = await fetch(`http://localhost:8080/file/getFileByName?fileId=${item.dataset.fileid}`,
                        {
                            method: "post",
                            headers: {
                                "Content-Type": "application/json"
                            },
                        })

                    if (!response.ok) {
                        return;
                    }

                    response = await response.blob();
                    response = URL.createObjectURL(response);
                    window.open(response, "_blank");

                } catch (error) {
                    throw new Error(error.message);
                }
            })
        })

        //Top performers
        try {
            await fetch("http://localhost:8080/performers/topPerformers")
                .then((res) => res.json())
                .then((data) => {
                    if(data.length===0)
                    {
                        document.querySelector(".top-performer").classList.add("display");
                        return;
                    }
                    let counter = 1;
                    data.forEach((performer) => {
                        let ul = document.createElement("ul");
                        ul.className = "leaderboard";

                        ul.innerHTML = `
                     <li class="performer">
                    <div class="rank first">${counter}</div>
                    <div class="performer-info">
                    <div class="name">${performer[0]}</div>
                    <div class="details">based on cumulative score</div>
                    </div>
                    <div class="score">Total Points: ${performer[1]}</div>
                    </li>`
                        document.querySelector(".top-performer").appendChild(ul);
                        counter++;
                    })


                })
        } catch (error) {
            throw new Error(error.message);
        }

        //Logoutbtn alert
        document.querySelector(".logout-btn").addEventListener("click",(e)=>{
            e.preventDefault();
            Swal.fire({
                title: "Are you sure?",
                text: "You want to LogOut !",
                icon: "warning",
                position:'top',
                showCancelButton: true,
                confirmButtonColor: "#3085d6",
                cancelButtonColor: "#d33",
                confirmButtonText: "Yes, Logout!"
            }).then((result) => {
                if (result.isConfirmed) {
                    // Submit the form manually
                    document.querySelector(".logout-btn").closest("form").submit();
                }
            });

        })
})

//toggle-sidebar
document.querySelector(".toggle-btn").addEventListener("click",()=>{
        document.querySelector(".side-bar").classList.toggle("active");
})

