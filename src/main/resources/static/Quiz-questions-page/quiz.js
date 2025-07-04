document.addEventListener("DOMContentLoaded",async ()=>{

    const quizType=localStorage.getItem("quizType");
    const form=document.querySelector("form");
    const submitBtn=document.querySelector(".submit-btn");
    const quizHeading=document.querySelector(".quiz-heading");
    const input=document.querySelector("input");
    const scorePara=document.querySelector(".score");
    const wrongQueBtn=document.querySelector(".wrongQueBtn");
    const quizWrapper=document.querySelector(".quiz-wrapper");
    const scorllWrapper=document.querySelector(".scroll-wrapper");
    const wrongAnsRevHeading=document.querySelector(".wrong-ans-heading");


    quizHeading.innerHTML=`🧠 ${quizType.toUpperCase()} QUIZ`
    let queCounter=1;
    let wrongQueCounter=1;
    let processData;
    let storedAns=[];
    let totalQuestions=0;

    function escapeHTML(str) {
        return str
            .replace(/&/g, "&amp;")
            .replace(/</g, "&lt;")
            .replace(/>/g, "&gt;")
            .replace(/"/g, "&quot;")
            .replace(/'/g, "&#039;");
    }


    try{
        await fetch(`http://localhost:8080/quiz/getQueByType?questionType=${quizType.toLowerCase()}`)
            .then((res)=>{
                if(!res.ok){
                    throw new Error("fetching failed"+res.statusText)
                }
                return  res.json();

            }).then((data)=>
            {
                processData=data;
                // console.log(data)
                data.forEach((quiz)=>{
                    const questionsDiv=document.createElement("div");
                    questionsDiv.className="questions";

                    const opt0 = escapeHTML(quiz.options[0].opt);
                    const opt1 = escapeHTML(quiz.options[1].opt);
                    const opt2 = escapeHTML(quiz.options[2].opt);
                    const opt3 = escapeHTML(quiz.options[3].opt);
                    const question = escapeHTML(quiz.question);


                    questionsDiv.innerHTML=`
                    <hr/>
                    <h4>${queCounter}. ${question}</h4>
                    <div class="options">
                    <label><input type="radio" name="q${queCounter}" value="${quiz.options[0].opt}">${opt0} </label>
                    <label><input type="radio" name="q${queCounter}" value="${quiz.options[1].opt}" > ${opt1}</label>
                    <label><input type="radio" name="q${queCounter}" value="${quiz.options[2].opt}" >${opt2}</label>
                    <label><input type="radio" name="q${queCounter}" value="${quiz.options[3].opt}" > ${opt3}</label>
                
                    </div>`
                    queCounter++;
                    form.insertBefore(questionsDiv,submitBtn);
                    // console.log(input.name);

                })
            })

            //set Timer
            const timer=document.querySelector(".timer");
            let time=10*60;
            const countDown=setInterval(()=>{
            let minutes=Math.floor(time/60);
            let seconds=time%60;

            timer.textContent=`Time Left: ${minutes.toString().padStart(2,'0')}:${seconds.toString().padStart(2,'0')}`;

            if(time===0)
            {
                clearInterval(countDown);
                Swal.fire({
                    text:"Time up, submitting your quiz",
                    position:'top'
                })

                totalQuestions=queCounter-1;
                let allAnswered=true;

                for(let i=0;i<totalQuestions;i++)
                {
                    const selected=document.querySelector(`input[name="q${i+1}"]:checked`);
                    if(!selected)
                    {
                        allAnswered=false;
                        Swal.fire({
                            text:"submission failed ! You have not selected all the answers",
                            position: 'top'
                        }).then(()=>{
                            window.location.href="/home-page/index.html";

                        })
                        return;

                    }
                }
                if(allAnswered){
                    form.dispatchEvent(new Event("submit"));
                }

            }
            time--;
        },1000)

        //submitting the form after clicking on submit btn
        form.addEventListener("submit",(e)=>{
                e.preventDefault();

                totalQuestions=queCounter-1;
            let allAnswered=true;

            for(let i=0;i<totalQuestions;i++)
            {
                const selected=document.querySelector(`input[name="q${i+1}"]:checked`)
                if(!selected)
                {
                    allAnswered=false;
                    Swal.fire({
                        text:"Quiz submission failed. Ensure all questions are answered before submitting",
                        position:'top'
                    })
                    storedAns=[];
                    console.log(storedAns)
                    break;
                }
                else{
                   const selectedAns=selected.value;
                   const questionId=processData[i].id;
                    storedAns.push({questionId:questionId,
                    answer:selectedAns});
                    localStorage.setItem(questionId,selectedAns);
                   console.log(storedAns);
                }
            }
        if(allAnswered){
            clearInterval(countDown)
            sendAnswers(storedAns,totalQuestions);
            Swal.fire({
                text:"Get Ready for the Score",
                position:'top'
            }).then(()=>{
                //what will happen after user submits the form???
                form.classList.add("display");
                scorePara.classList.remove("display");
                wrongQueBtn.classList.remove("display");
            })

        }})

        //function to send the answers
        async function sendAnswers(storedAns,totalQuestions)
        {
            const quizId=localStorage.getItem("quizId")
            try{
                let response=await fetch(`http://localhost:8080/validateAns/getScore?quizId=${parseInt(quizId)}`,
                    {
                        headers:{
                            "Content-Type":"application/json"
                        },
                        method:"post",
                        body:JSON.stringify(storedAns)
                    })

                if(!response.ok){
                    Swal.fire({
                        text:"failed to get score",
                        position:'top'
                    })
                    return
                }

                let score=await response.json();

                console.log(score);
                scorePara.innerText=`Your score is ${score} out of ${totalQuestions}`;

                //if all answers are correct then remove the wrongAnsBtn
                if(score===totalQuestions)
                {
                    wrongQueBtn.classList.add("display");
                }

            }catch(error)
            {
                throw new Error(error.message);
            }
        }

        // wrong answer review
        wrongQueBtn.addEventListener("click",async ()=>{
            quizWrapper.classList.add("display");
            await getWrongAns();
        })

        //function to get wrong answers
        async function getWrongAns()
        {
            try{
                let response=await fetch("http://localhost:8080/validateAns/getWrongAns")

                if(!response.ok)
                {
                    Swal.fire({
                        text:"failed to get wrong answers!!",
                        position:'top'
                    })
                    return
                }

                response=await response.json();
                console.log(response)
                let correctOpt;

                response.forEach((data)=>{

                    let que=data.question;
                    console.log(que)

                   let options=data.options;
                   options.forEach((option)=>{
                       console.log(option.opt);
                       if(option.isCorrect)
                       {
                           correctOpt=option.opt;
                           console.log("correct option: "+correctOpt);
                       }
                   })

                    let selectedAns=localStorage.getItem(data.id)
                    console.log("selected option: "+ selectedAns)
                    wrongAnsRevHeading.classList.remove("display");
                    const questionContainer=document.createElement("div");
                   questionContainer.className="question-container";

                   questionContainer.innerHTML=`
                    <p class="question">Q${wrongQueCounter}. ${escapeHTML(que)}</p>
                    <ul class="options">
                    <li class="wrong">Your Answer: ${escapeHTML(selectedAns)}</li>
                    <li class="correct">Correct Answer: ${escapeHTML(correctOpt)}</li>
                    </ul>`
                    wrongQueCounter++;
                    scorllWrapper.appendChild(questionContainer);
                })
            }
            catch(error)
            {
                throw new Error(error.message);
            }

        }
    }catch (error){
        throw new Error(error.message);
    }
})