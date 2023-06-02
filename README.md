# PMC Explorer

## Enhanced biomedical data extraction from scientific publications


This prototype was developed as part of a Masters Thesis in Bioinformatics at the University of Bergen. 

The objective of this prototype is to enhance the process of extracting biomedical data from scientific publications. 
This prototype can search and extract data from the PubMed Central Open Acess (PMC-OA) subset of the PubMed database.
This enables researchers to quickly and efficiently process large volumes of text and can facilitate the 
identification of trends and patterns across multiple studies.

## Description

Because of the prototype's ability to explore articles in PubMed Central, it has been given the name "PMC Explorer".

The prototype has two main functionalities:

i) Retrieving articles from the PMC-OA subset of PubMed using an input query and 
determineing the relevancy of the retrieved articles.
 
ii) Inspecting an article and see why the article was determined as relevant.


## Getting Started

**1)** Locate and download the jar file. 

**2)** After downloading the jar file check where it is located on your computer. This is 
important because when running the tool a new folder called "PMC_Explorer_Downloads" will
be generated in the same folder as the local jar file. When performing a query in the tool
the articles will be downloaded in this folder. 



## Executing program

Next, a step by step guide to using the tool will be provided:

-------------------------------------------------------------------------------------------------------------------

### Downloading and exploring articles


**3)** When first initializing the tool there will be no local articles to inspect. To download 
articles the button "Browse PubMed" has to be marked. When performing a search, keywords 
are separated using the operator ",". It is adviced to not use more than 5 keywords when building
the keyword queries. 

**4)** When the query has been built, click the "RUN" button. This will initialize the search in PubMed 
and retrieve the top 20 most relevant articles to the input keywords. Each article will be 
downloaded locally so this will take some time. 

**5)** After downloading the articles, a table containing the meta-data of the downloaded articles will appear.
This includes authors, title, year and PMC-id. In addition, there will be a column called "Notes", "Status" 
"Frequencies" and "Inspect Article". 

"Notes" -> By clicking this cell you will be able to access the article's unique txt file which is created
when downloading the article. 

"Status" -> This will present the status of the article. Green indicating that the article is relevant and
yellow indicating that it is not.

"Frequencies" -> This will show a box for each keyword used in the query. The darker the shade of green, the 
more frequent the keyword is in the article.

"Inspect Article" -> This will take you to the inspection part of the prototype. 

**6)** After downloading the articles, the user can change the query to see how the choice of keywords impact 
the relevancy of the articles. This can be done by marking the button "Browse local files" and clicking "RUN". 

**7)** To further inspect an article click the "Inspect Article" button in the row of the article you wish to
inspect. This will open a new window:

-------------------------------------------------------------------------------------------------------------------

### Inspecting an article

**8)** When inspecting an article the text is split into four tabs. The first tab displays the 
abstract of the article, the second displays the body, the third displays the relevant sentences and the fourth
displays the tables.

**9)** When Regarding the "SENTENCES" tab there is one point to mention. This tab contains only the sentences of
the text which include one of the input keywords. By default these keywords are separated by ",". However, by using
the operator "+" between multiple words this will display the sentences which include all of the words that are
chained together with this operator. 

**10)** For each text panel there will appear two associated tables:
One table displaying the keyword frequencies and one table displaying the most frequent words of the text segment.
These tables can give an overview of how frequent the input keywords are in comparison with the most frequent
words of the text. The words in both tables can be selected, resulting in the words beeing highlihted in the text.
By default the keywords are highlighted in four different colors. It is possible to select, unselect or change
highlight color of a word/keyword in the table. 


**11)** In the table panel the tables that are provided as part of the text will be listed. Here a user can 
inspect and extract data from the tables. By holding "ctrl" and selecting table columns will open the 
possibility of storing the selected columns. By clicking "SAVE", the selected columns will be saved in the 
article's own txt file. 







## Author

Markus Almendral Berggrav


## Version History

* 1.0
    * Initial Release - 01/06/2023 (1. of June 2023)
    * Final adjustments and README - 02/06/2023 (2. of June 2023)
