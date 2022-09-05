# Loan Data Exploration
## by Nkomba A Koung


## Dataset

> The data set that I analyze, is named "ProsperLoanData"; it contains 113,937 loans with 81 columns on each loan, including loan amount, borrower rate (interest rate), current loan status, borrower income, and many others. The Analysis of this dataset consist in: <br/> 
<ul>
    <li>
        The Data Wrangling steps: including the collection of the data, the Assessing and Cleaning of him. During the cleaning, I selected the columns that I would use in the EDA step, I replaced the Nan values in the EmploymentStatus variable by the value 'Not Available', and I changed the type of the listingCategory (numeric) variable.
    </li> 
    <li>
        The EDA (Exploratory Data Analysis) step: which consist in the Univariate analysis, Bivariate Analysis and Multivariate Analysis.
    </li>
</ul>


## Summary of Findings

> Considering the finding in this Exploratory Analysis, I discover these results: <br/>
<ul type = "square">
    <li>
        During the Univariate Analysis: 
        <ol>
            <li>
                The variables which follow a normal distribution are: EmploymentStatusDuration, DebtToIncome, MonthlyLoanPayment, ProsperPrincipalOutstanding, ScoreexChangeAtTimeOfListing, LP_InterestandFees, Term and OpenRevolvingAccounts.
            </li>
            <li>
                Most of the borrower belong to the group who has the following properties:
                <ul>
                    <li>LoanStatus: Current, Completed</li>
                    <li>BorrowerState: CA, TX</li>
                    <li>Listing Category: Debt Consolidation, Not Available</li>
                    <li>EmploymentStatus: Employed, Full-Time</li>
                    <li>IncomeRange: $25K - $50K, $50k - $75k</li>
                    <li>Occupation: "Other", "Professional", or "Computer Programmer"</li>
                    <li>CurrentlyInGroup: False</li>
                    <li>IsBorrowerHomeowner: True</li>
                </ul>
            </li>
        </ol>
    </li>
    <li>
        During, the Bivariate Analysis, I had these results:
        <ol>
            <li>
                Regarding the board of correlation, these variables have a relationship:<br/>
                <ul>
                    <li>Term and LoanNumber</li>
                    <li>Term and LoanOriginalAmount</li>
                    <li>IsBorrowerHomeowner and OpenRevolvingAccount</li>
                    <li>LoanMonthsSinceOrigination and CurrentlyInGroup</li>
                    <li>IsBorrowerHomeowner and LoanOriginalAmount</li>
                    <li>MonthlyLoanPayment and LP_interestFees</li>
                    <li>MonthlyLoanPayment and LoanOriginalAmount</li>
                    <li>Negative correlation between BorrowerRate and MonthlyLoanPayment</li>
                    <li>LoanNumber and LoanMonthSinceOrigination</li>
                </ul>
            </li>
            <li>
                According to the description of the loanStatus by each Numerical variables, I had discover this results:<br/>
                <ul>
                    <li>
                        When the employment status duration is not between 75 and 125 months in average, then the loan is cancelled and when the spread of the employment status duration is high the loan is in "past due" state
                    </li>
                    <li>
                        If the debt to income ratio doesn't really vary as go far of 0.3, the loan status is "current" or "completed"
                    </li>
                    <li>
                        If the length of the loan is too few, his status is cancelled or completed or Defaulted. 
                    </li>
                    <li>
                        If the Loan Original Amount is lower than $6k, then the loan status is Cancelled 
                    </li>
                    <li>
                        Higher is the Monthly Loan payment, lower are the chance to cancel the Loan, and depending of the variability of the MonthlyLoanPayment, the loan can have the "past due" state or not.
                    </li>
                    <li>
                        If the mean number of recommendation is greater than 0.06, the loan will have the status charged off, Defaulted, or Completed
                    </li>
                    <li>
                        Lower is the mean Borrower Rate, Higher is the chance that the loan status is Completed
                    </li>
                </ul>
            </li>
            <li>
                the majority of the borrower in the loan status of Current and Completed have the Income Range in these intervals: [$25k - $50k], [$50k - $75k], [$1 - $25k], [$75k - $100k] and $100k+
            </li>
            <li>
                Most of the borrower in the current loan status are Employed and those in the Completed status are Employed and Full-time worker
            </li>
            <li>
                Most of the borrower in the Current loan status bind to the 'Other', 'Debt Consolidation', and 'Household Expenses' listing category
            </li>
            <li>
                Most of borrower in the charged off and Completed loan status belong to the 'Other', 'Professional', 'Sales - commission', 'Analyst', and 'Computer Programmer' Occupation
            </li>
            <li>
                The borrower who have completed their loan are in following state: CA, FL, IL, NY, TX, and GA
            </li>
        </ol>
    </li>
    <li>
        During the Multivariate Analysis, I discovered these results:
        <ol>
            <li> 
                When the mean MonthlyLoanPayment is higher, then the loan status is current and the IncomeRange can vary in the range like [$25k - $50k[, [$50k - $75k[, $100k+, and [$75k - $100k[
            </li>
            <li>
                The borrower who are HomeOwner has the lower BorrowerRate than the borrower who's not. Moreover, Worker like the Biologist, Teacher's Aide, and Bus Driver have the higher Borrower rate whatever they are homeowner or not so the are not reliable. Otherwise, the borrowers, who has the occupations of Nurse, judge or homeMaker, have the lowest borrowerRate and more lower when they are Homeowner, then they are reliable
            </li>
            <li>
                When the LoanOriginalAmount is higher as the MonthlyLoanPayment, then the loan status is Completed, and if the LoanOriginalAmount increase more faster than the MonthlyLoanPayment, then the loan status have big chance to be Current
            </li>
        </ol>
    </li>
</ul>


## Key Insights for Presentation

> The Key Insights discover during all this analysis are the following: <br/>
<ul type = "square">
    <li>
        The Borrower Rate is affected by tht LP_InterestandFees, so when the LP_InterestandFees grow, then the Borrower Rate grow also
    </li>
    <li>
        To have a loan status to 'Completed', the borrower should possess the following properties: <br/>
        <ol>
            <li>The EmploymentStatusDuration between 75 and 100 Months</li>
            <li>A constant DebtToIncome Ratio</li>
            <li>A few number of Month to pay off the loan</li>
            <li>A loanOriginalAmount near of $6k</li>
            <li>An Higher number of Recommendations</li>
            <li>A MonthlyLoanPayment close of 200</li>
            <li>The lowest BorrowerRate</li>
        </ol>
    </li>
    <li>
        Regarding the qualitative properties, A loan status is "Completed" when:
        <ol>
            <li>
                The Income Range in these intervals: [$25k - $50k], [$50k - $75k], [$1 - $25k], [$75k - $100k] and $100k+
            </li>
            <li>
                The borrower belong to the EmploymentStatus Employed and Full-time
            </li>
            <li>
                The ListingCategory are 'Other', 'Debt Consolidation', or 'Household Expenses'
            </li>
            <li>
                The Occupation belong in this set: 'Other', 'Professional', 'Sales - commission', 'Analyst', and 'Computer Programmer'
            </li>
        </ol>
    </li>
    <li>
        The BorrowerRate is more higher for the borrower who isn't a homeowner
    </li>
</ul>





