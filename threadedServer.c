#include <sys/types.h>
#include <sys/wait.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <unistd.h>
#include <netdb.h>
#include <signal.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>
#include <pthread.h>
#include <ctype.h>

#define SERVER_PORT 1234
#define QUEUE_SIZE 5
#define BUF_SIZE 100
#define MAX_USERS 100
#define MAX_USER_LENGTH 100
#define MAX_TOPIC 100
#define MAX_POST 10

//GLOBAL VARIABLES
int followTopic[MAX_TOPIC][MAX_TOPIC];
int followCount[MAX_TOPIC];
char* username[MAX_USERS];
char* password[MAX_USERS];
char* topic[MAX_TOPIC];
char* posts[MAX_TOPIC][MAX_POST];
int countPost[MAX_TOPIC];
int countUsers = 0;
int countTopic = 0;

//struktura zawierająca dane, które zostaną przekazane do wątku
struct thread_data_t
{
    int descriptor;
};

void changeUpper(char* tmp, int n){
    for(int i=0 ; i<n ; i++){
        tmp[i] = toupper(tmp[i]);
    }
}
void fillZeros(){
    for(int i=0 ; i<MAX_TOPIC ; i++){
        followCount[i] = 0;
        countPost[i] = 0;
    }
}

int returnIndex(char* tmp){
    int j = 0;
    int flag = 1;
    int index = -1;
    for(int i=0 ; i<countUsers ; i++){
        j = 0;
        flag = 1;
        while(tmp[j]!='\n' && username[i][j]!= '\n'){
            if(tmp[j] != username[i][j]){
                flag = 0;
                break;
            }
            j++;
        }
        if(flag == 1){
            index = i;
            break;
        }
    }
    return index;
}

//add topic to database
void addTopic(struct thread_data_t *th_data){

    char* buff = (char*)malloc(BUF_SIZE * sizeof(char));

    int readOutput = read(th_data->descriptor, buff, BUF_SIZE);

    topic[countTopic] =  (char*)malloc(BUF_SIZE);

    strcpy(topic[countTopic],buff);

    countTopic++;
}
void addPost(struct thread_data_t *th_data, int idx){

    char* buff = (char*)malloc(BUF_SIZE * sizeof(char));

    int readOutput = read(th_data->descriptor, buff, BUF_SIZE);

    posts[idx - 1][countPost[idx - 1]] =  (char*)malloc(BUF_SIZE);

    strcpy(posts[idx - 1][countPost[idx - 1]],buff);

    printf("Dodany post: %s", posts[idx - 1][countPost[idx - 1]]);

    countPost[idx - 1]++;
}
void printTopics(){
    printf("%d\n", countTopic);
    for(int i=0 ; i<countTopic ; i++){
        printf("Topic: -%s", topic[i]);
    }
}

void printUsers(){
    printf("%d\n", countUsers);
    for(int i=0 ; i<countUsers ; i++){
        printf("Uzytkownik: -%s-%s-\n", username[i], password[i]);
    }
}

int checkIfExists(char* login, char* pass) {
    
    
    for(int i=0 ; i<countUsers ; i++){
        if(strcmp(login, username[i]) == 0){
            if(strcmp(pass, password[i]) == 0){
                return 1;
            }
        }
    }
    
    return 0;
}

int readCurrentUser(struct thread_data_t *th_data){

    char* buff = (char*)malloc(BUF_SIZE * sizeof(char));
    char* buff1 = (char*)malloc(BUF_SIZE * sizeof(char));

    int readOutput = read(th_data->descriptor, buff, sizeof(buff));
    char* login = (char*)malloc(readOutput);
    strcpy(login,buff);


    readOutput = read(th_data->descriptor, buff1, sizeof(buff1));
    char* pass = (char*)malloc(readOutput);

    strcpy(pass,buff1);
    /*
    for(int i=0 ; i<readOutput-1;i++){
        pass[i]= buff1[i];
    }
    */
    int result = checkIfExists(login, pass);

    return result;

}

void readUsername(struct thread_data_t *th_data){

    char* buff = (char*)malloc(BUF_SIZE * sizeof(char));
    char* buff1 = (char*)malloc(BUF_SIZE * sizeof(char));

    int readOutput = read(th_data->descriptor, buff, sizeof(buff));

    //tutaj funkcja co sprawdza czy moze byc taki nick
    username[countUsers] =  (char*)malloc(readOutput);

    strcpy(username[countUsers],buff);


    if(readOutput > 0){
        printf("Nowa wiadomosc od clienta %d\n",th_data->descriptor);
    }
    else{
        printf("Blad podczas odbioru wiadomosci");
    }
    printf("Rejestracja uzytkownika LOGIN -> %s", buff);

    readOutput = read(th_data->descriptor, buff1, sizeof(buff1));
    password[countUsers] =  (char*)malloc(readOutput);
    strcpy(password[countUsers],buff1);


    if(readOutput > 0){
        printf("Nowa wiadomosc od clienta %d\n",th_data->descriptor);
    }
    else{
        printf("Blad podczas odbioru wiadomosci");
    }
    printf("Rejestracja uzytkownika HASLO-> %s", buff1);

    countUsers++;
    
}

void respondUsername(struct thread_data_t *th_data){
    char buff[7] = "Success";

    int readOutput = write(th_data->descriptor, buff, sizeof(buff));
    
    if(readOutput > 0){
        printf("Nowa wiadomosc dla clienta %d\n",th_data->descriptor);
    }
    else{
        printf("Blad podczas odbioru wiadomosci");
    }
    
}

void sendResult(struct thread_data_t *th_data, int d){
    char buff[7] = "Success";
    char buff1[7] = "Failure";

    int readOutput;

    if(d == 1){
        readOutput = write(th_data->descriptor, buff, sizeof(buff));
    }
    else if(d == 0){
        readOutput = write(th_data->descriptor, buff1, sizeof(buff1));
    }

}

//funkcja opisującą zachowanie wątku - musi przyjmować argument typu (void *) i zwracać (void *)
void *ThreadBehavior(void *t_data)
{
    pthread_detach(pthread_self());
    struct thread_data_t *th_data = (struct thread_data_t*)t_data;
    char* buff = (char*)malloc(BUF_SIZE * sizeof(char));
    char* buff1 = (char*)malloc(BUF_SIZE * sizeof(char));
    char* buff2 = (char*)malloc(BUF_SIZE * sizeof(char));
    char* buff3 = (char*)malloc(12 * sizeof(char));
    buff3 = "Brak tematow";

    char* tmpCnt;

    puts("Witamy w serwerze");

    int readOutput = read(th_data->descriptor, buff, sizeof(buff));
    int decision = atoi(buff);


    //REJESTRACJA
    if(decision == 1){
        readUsername(th_data);
        respondUsername(th_data);
    }
    //LOGOWANIE
    else if(decision == 2){
        //CHECK IF EXISTS IN THE SYSTEM
        int result = readCurrentUser(th_data);
        sendResult(th_data,result);

        printUsers();
        //FUNCTIONALITY
        if(result == 1){
            readOutput = read(th_data->descriptor, buff, sizeof(buff));
            decision = atoi(buff);


            //add new topic
            if(decision == 1){
                puts("Uzytkownik chce stworzyc temat\n");
                addTopic(th_data);
                printTopics();
            }
            //sub topic
            else if(decision == 2){
                sprintf(tmpCnt, "%d",countTopic);
                readOutput = write(th_data->descriptor, tmpCnt, sizeof(tmpCnt));

                if(countTopic > 0){

                    for(int i=0 ; i<countTopic ; i++){
                        readOutput = write(th_data->descriptor, topic[i], BUF_SIZE);
                    }

                    //read decyzje tematu
                    readOutput = read(th_data->descriptor, buff2, sizeof(buff2));
                    decision = atoi(buff2);

                    //read komu przypisac
                    //char* buff3 = (char*)malloc(BUF_SIZE);
                    readOutput = read(th_data->descriptor, buff2, sizeof(buff2));
                    /*
                    char* buff2 = (char*)malloc(readOutput);
                    for(int i=0 ; i<readOutput-1;i++){
                        buff2[i] = buff[i];
                    }
                    */
                    int userIndex = returnIndex(buff2);

                    followTopic[userIndex][followCount[userIndex]] = decision - 1;
                    followCount[userIndex] += 1;

                    printf("Wybrany temat: %d\n", decision - 1);
                    printf("Wybrany uzytkownik: %s\n", buff2);

                    puts("Udalo sie!\n");
                }
                else{
                    readOutput = write(th_data->descriptor, buff3, 12);
                }
                
            }
            //new post
            else if(decision == 3){
                puts("Uzytkownik chce dodac posta");
                sprintf(tmpCnt, "%d",countTopic);
                readOutput = write(th_data->descriptor, tmpCnt, sizeof(tmpCnt));

                if(countTopic > 0){
                    
                    for(int i=0 ; i<countTopic ; i++){
                        readOutput = write(th_data->descriptor, topic[i], BUF_SIZE);
                    }

                    //read decyzje tematu
                    readOutput = read(th_data->descriptor, buff, sizeof(buff));
                    decision = atoi(buff);
                    
                    //printf("Wybrany temat: %d\n", decision);
                    
                    addPost(th_data, decision);
                }
                else{
                    readOutput = write(th_data->descriptor, buff3, 12);
                }

            }
            else if(decision == 4) {
                puts("Uzytkownik chce zobaczyc tablice\n");

                readOutput = read(th_data->descriptor, buff1, sizeof(buff1));
                int idxUser = returnIndex(buff1);
                int flag = 1;

                if(followCount[idxUser] > 0){
                    flag = 0;
                    for(int i = 0 ; i < followCount[idxUser] ; i++){
                        int topicIndex = followTopic[idxUser][i];
                        changeUpper(topic[topicIndex], BUF_SIZE);
                        readOutput = write(th_data->descriptor, topic[topicIndex], BUF_SIZE);

                        
                        for(int j=0 ; j< countPost[topicIndex] ; j++){
                            readOutput = write(th_data->descriptor, posts[topicIndex][j], BUF_SIZE);
                        }
                        
                    }
                }
                else{
                    readOutput = write(th_data->descriptor, buff3, 12);
                }
                
            }
            else{
                puts("NOT IMPLEMENTED\n");
            }
        }
        else{
            puts("Blad podczas logowania");
        }

    }

    else{
        printf("Blad wyboru");
    }

    //printUsers();

    pthread_exit(NULL);
}

//funkcja obsługująca połączenie z nowym klientem
void handleConnection(int connection_socket_descriptor) {
    //wynik funkcji tworzącej wątek
    int create_result = 0;

    //uchwyt na wątek
    pthread_t thread1;

    //dane, które zostaną przekazane do wątku
    struct thread_data_t *t_data = malloc(sizeof(struct thread_data_t));
    t_data->descriptor = connection_socket_descriptor;
    //TODO dynamiczne utworzenie instancji struktury thread_data_t o nazwie t_data (+ w odpowiednim miejscu zwolnienie pamięci)
    //TODO wypełnienie pól struktury

    create_result = pthread_create(&thread1, NULL, ThreadBehavior, (void *)t_data);
    if (create_result){
       printf("Błąd przy próbie utworzenia wątku, kod błędu: %d\n", create_result);
       exit(-1);
    }
    //TODO (przy zadaniu 1) odbieranie -> wyświetlanie albo klawiatura -> wysyłanie
    //free(t_data);
}

int main(int argc, char* argv[])
{
   fillZeros();
   int server_socket_descriptor;
   int connection_socket_descriptor;
   int bind_result;
   int listen_result;
   char reuse_addr_val = 1;
   struct sockaddr_in server_address;

   epoll_create1(0);

   //inicjalizacja gniazda serwera
   
   memset(&server_address, 0, sizeof(struct sockaddr));
   server_address.sin_family = AF_INET;
   server_address.sin_addr.s_addr = htonl(INADDR_ANY);
   server_address.sin_port = htons(SERVER_PORT);

   server_socket_descriptor = socket(AF_INET, SOCK_STREAM, 0);
   if (server_socket_descriptor < 0)
   {
       fprintf(stderr, "%s: Błąd przy próbie utworzenia gniazda..\n", argv[0]);
       exit(1);
   }
   setsockopt(server_socket_descriptor, SOL_SOCKET, SO_REUSEADDR, (char*)&reuse_addr_val, sizeof(reuse_addr_val));

   bind_result = bind(server_socket_descriptor, (struct sockaddr*)&server_address, sizeof(struct sockaddr));
   if (bind_result < 0)
   {
       fprintf(stderr, "%s: Błąd przy próbie dowiązania adresu IP i numeru portu do gniazda.\n", argv[0]);
       exit(1);
   }

   listen_result = listen(server_socket_descriptor, QUEUE_SIZE);
   if (listen_result < 0) {
       fprintf(stderr, "%s: Błąd przy próbie ustawienia wielkości kolejki.\n", argv[0]);
       exit(1);
   }

   while(1)
   {
       connection_socket_descriptor = accept(server_socket_descriptor, NULL, NULL);
       if (connection_socket_descriptor < 0)
       {
           fprintf(stderr, "%s: Błąd przy próbie utworzenia gniazda dla połączenia.\n", argv[0]);
           exit(1);
       }

       handleConnection(connection_socket_descriptor);
   }

   close(server_socket_descriptor);
   return(0);
}
