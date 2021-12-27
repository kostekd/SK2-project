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

#define SERVER_PORT 1235
#define QUEUE_SIZE 5
#define BUF_SIZE 100
#define MAX_USERS 100
#define MAX_USER_LENGTH 100

//GLOBAL VARIABLES
char* username[MAX_USERS];
char* password[MAX_USERS];
int countUsers = 0;

//struktura zawierająca dane, które zostaną przekazane do wątku
struct thread_data_t
{
    int descriptor;
};

void printUsers(){
    for(int i=0 ; i<countUsers ; i++){
        printf("%d\n", countUsers);
        printf("Uzytkownik: -%s-%s-\n", username[i], password[i]);
    }
}

int checkIfExists(char* login, char* pass) {
    
    
    for(int i=0 ; i<countUsers ; i++){
        printf("%s %s", login, username[i]);
        if(strcmp(login, username[i]) == 0){
            //printf("%s %s", login, username[i]);
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
    // SPRAWDZAMY CZY readOutput dziala
    for(int i=0 ; i<readOutput-1;i++){
        login[i]= buff[i];
    }

    readOutput = read(th_data->descriptor, buff1, sizeof(buff1));
    char* pass = (char*)malloc(readOutput);

    for(int i=0 ; i<readOutput-1;i++){
        pass[i]= buff1[i];
    }
    int result = checkIfExists(login, pass);

    return result;

}

void readUsername(struct thread_data_t *th_data){

    char* buff = (char*)malloc(BUF_SIZE * sizeof(char));
    char* buff1 = (char*)malloc(BUF_SIZE * sizeof(char));

    int readOutput = read(th_data->descriptor, buff, sizeof(buff));

    //tutaj funkcja co sprawdza czy moze byc taki nick
    username[countUsers] =  (char*)malloc(readOutput);
    //username[countUsers] = buff;
    for(int i=0 ; i<readOutput-1;i++){
        username[countUsers][i] = buff[i];
    }

    printf("Rozmiar: %d", readOutput);

    if(readOutput > 0){
        printf("Nowa wiadomosc od clienta %d\n",th_data->descriptor);
    }
    else{
        printf("Blad podczas odbioru wiadomosci");
    }
    printf("Rejestracja uzytkownika LOGIN -> %s", buff);

    readOutput = read(th_data->descriptor, buff1, sizeof(buff1));
    password[countUsers] =  (char*)malloc(readOutput);
    //password[countUsers] = buff1;
    for(int i=0 ; i<readOutput-1;i++){
        password[countUsers][i] = buff1[i];
    }

    printf("Rozmiar: %d", readOutput);

    if(readOutput > 0){
        printf("Nowa wiadomosc od clienta %d\n",th_data->descriptor);
    }
    else{
        printf("Blad podczas odbioru wiadomosci");
    }
    printf("Rejestracja uzytkownika HASLO-> %s", buff1);

    countUsers++;
    printUsers();
    
}

void respondUsername(struct thread_data_t *th_data){
    char buff[11] = "Jebac Souse";

    int readOutput = write(th_data->descriptor, buff, sizeof(buff));
    
    if(readOutput > 0){
        printf("Nowa wiadomosc dla clienta %d\n",th_data->descriptor);
    }
    else{
        printf("Blad podczas odbioru wiadomosci");
    }
    
}

void sendResult(struct thread_data_t *th_data, int d){
    char buff[6] = "Supers";
    char buff1[6] = "Szkoda";

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
        char buff[BUF_SIZE];

    //dostęp do pól struktury: (*th_data).pole
    //TODO (przy zadaniu 1) klawiatura -> wysyłanie albo odbieranie -> wyświetlanie
    int readOutput = read(th_data->descriptor, buff, sizeof(buff));
    printf("Decyzja: %s\n", buff);
    int decision = atoi(buff);

    //REJESTRACJA
    if(decision == 1){
        readUsername(th_data);
        respondUsername(th_data);
    }
    else if(decision == 2){
        //NOT IMPLEMENTED
        int result = readCurrentUser(th_data);
        sendResult(th_data,result);
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
   int server_socket_descriptor;
   int connection_socket_descriptor;
   int bind_result;
   int listen_result;
   char reuse_addr_val = 1;
   struct sockaddr_in server_address;

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
