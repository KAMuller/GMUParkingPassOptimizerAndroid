//
// Created by Kalman Muller on 11/30/2018.
// Algorithm provided by GeeksforGeeks
// https://www.geeksforgeeks.org/dijkstras-algorithm-for-adjacency-list-representation-greedy-algo-8/
//
#include <jni.h>
#include <stdlib.h>
#include <stdio.h>
#include <string.h>


#define MAX_INT 999999999

typedef struct AdjNode_ {
    int index; //represents vertex
    int weight; //represents edge distance
    struct AdjNode_* next; //linked list
} AdjNode;

typedef struct AdjList_ {
    AdjNode* head; //to access list
} AdjList;

typedef struct Graph_ {
    int V; //number of vertices
    AdjList* array; //array of llists
} Graph;

typedef struct MinHeapNode_{
    int v; //index in array
    int dist; //shortest path
} MinHeapNode;

typedef struct MinHeap_{
    int size;
    int capacity;
    int *pos;
    MinHeapNode **array;
} MinHeap;


MinHeapNode* newMinHeapNode(int v, int dist)
{
    MinHeapNode* minHeapNode = malloc(sizeof(MinHeapNode));
    minHeapNode->v = v;
    minHeapNode->dist = dist;
    return minHeapNode;
}


MinHeap* createMinHeap(int capacity)
{
    MinHeap* minHeap = malloc(sizeof(MinHeap));
    minHeap->pos = malloc(capacity * sizeof(int));
    minHeap->size = 0;
    minHeap->capacity = capacity;
    minHeap->array = malloc(capacity * sizeof(MinHeapNode*));
    return minHeap;
}

//adds an adjacent node to vertex of specified index
AdjNode* newAdjNode(int index, int weight)
{
    AdjNode* newNode = malloc(sizeof(AdjNode));
    newNode->index = index;
    newNode->weight = weight;
    newNode->next = NULL;
    return newNode;
}


Graph* createGraph(int V)
{
    Graph* graph = malloc(sizeof(Graph));
    graph->V = V;
    graph->array = malloc(V * sizeof(AdjList));

    for(int i = 0; i < V; i++)
    {
        graph->array[i].head = NULL;
    }
    return graph;
}


void addEdge(Graph* graph, int src, int dest, int weight)
{
    AdjNode* newNode = newAdjNode(dest, weight);
    newNode->next = graph->array[src].head;
    graph->array[src].head = newNode;

    newNode = newAdjNode(src, weight);
    newNode->next = graph->array[dest].head;
    graph->array[dest].head = newNode;
}

void swapMinHeapNode(MinHeapNode** a, MinHeapNode** b)
{
    MinHeapNode* t = *a;
    *a = *b;
    *b = t;
}

//percolate DOWN
void minHeapify(MinHeap* minHeap, int idx)
{
    int smallest, left, right;
    smallest = idx;
    left = 2 * idx + 1;
    right = 2 * idx + 2;

    if (left < minHeap->size &&
        minHeap->array[left]->dist < minHeap->array[smallest]->dist )
        smallest = left;

    if (right < minHeap->size &&
        minHeap->array[right]->dist < minHeap->array[smallest]->dist )
        smallest = right;

    if (smallest != idx)
    {
        // The nodes to be swapped in min heap
        MinHeapNode *smallestNode = minHeap->array[smallest];
        MinHeapNode *idxNode = minHeap->array[idx];

        // Swap positions
        minHeap->pos[smallestNode->v] = idx;
        minHeap->pos[idxNode->v] = smallest;

        // Swap nodes
        swapMinHeapNode(&minHeap->array[smallest], &minHeap->array[idx]);

        minHeapify(minHeap, smallest);
    }
}

int isEmpty(MinHeap* minHeap)
{
    if(minHeap->size == 0)
    {
        return 1;
    }
    else
    {
        return 0;
    }
}

//delete top and percolate top node DOWN
MinHeapNode* extractMin(MinHeap* minHeap)
{
    if (isEmpty(minHeap) == 1)
        return NULL;

    // Store the root node
    MinHeapNode* root = minHeap->array[0];

    // Replace root node with last node
    MinHeapNode* lastNode = minHeap->array[minHeap->size - 1];
    minHeap->array[0] = lastNode;

    // Update position of last node
    minHeap->pos[root->v] = minHeap->size-1;
    minHeap->pos[lastNode->v] = 0;

    // Reduce heap size and heapify root
    --minHeap->size;
    minHeapify(minHeap, 0);

    return root;
}

//PERCOLATE UP and update dist val
void decreaseKey(MinHeap* minHeap, int v, int dist)
{
    // Get the index of v in  heap array
    int i = minHeap->pos[v];

    // Get the node and update its dist value
    minHeap->array[i]->dist = dist;

    // Travel up while the complete tree is not heapified.
    // This is a O(Logn) loop
    while (i && minHeap->array[i]->dist < minHeap->array[(i - 1) / 2]->dist)
    {
        // Swap this node with its parent
        minHeap->pos[minHeap->array[i]->v] = (i-1)/2;
        minHeap->pos[minHeap->array[(i-1)/2]->v] = i;
        swapMinHeapNode(&minHeap->array[i],  &minHeap->array[(i - 1) / 2]);

        // move to parent index
        i = (i - 1) / 2;
    }
}

//if the vertex is in heap
int isInMinHeap(MinHeap *minHeap, int v)
{
    if (minHeap->pos[v] < minHeap->size)
        return 1;
    return 0;
}

int* dijkstra(Graph* graph, int src)
{
    int V = graph->V;
    int* dist = malloc(V * sizeof(int));//add check

    MinHeap* minHeap = createMinHeap(V);

    int x;
    for(x = 0; x < V; x++)
    {
        dist[x] = MAX_INT;
        minHeap->array[x] = newMinHeapNode(x, dist[x]);
        minHeap->pos[x] = x;
    }

    minHeap->array[src] = newMinHeapNode(src, dist[src]);
    minHeap->pos[src]   = src;
    dist[src] = 0;
    decreaseKey(minHeap, src, dist[src]);

    minHeap->size = V;

    while(isEmpty(minHeap) == 0)
    {
        MinHeapNode* minHeapNode = extractMin(minHeap);
        int u = minHeapNode->v;

        AdjNode* pCrawl = graph->array[u].head;
        while(pCrawl != NULL)
        {
            int v = pCrawl->index;
            if(isInMinHeap(minHeap, v) == 1 && dist[u] != MAX_INT && pCrawl->weight + dist[u] < dist[v])
            {
                dist[v] = pCrawl->weight + dist[u];
                decreaseKey(minHeap, v, dist[v]);
            }
            pCrawl = pCrawl->next;
        }
    }
    return dist;
}

int* calc(int index){
    //set v for the vertex of the graph
    int v = 33;


    Graph* graph = createGraph(v);

    addEdge(graph, 0, 24, 600);
    addEdge(graph, 0, 25, 640);
    addEdge(graph, 1, 16, 375);
    addEdge(graph, 2, 28, 235);
    addEdge(graph, 2, 30, 580);
    addEdge(graph, 2, 19, 550);
    addEdge(graph, 3, 23, 495);
    addEdge(graph, 4, 30, 400);
    addEdge(graph, 4, 10, 450);
    addEdge(graph, 5, 30, 850);
    addEdge(graph, 5, 26, 750);
    addEdge(graph, 6, 7, 730);
    addEdge(graph, 7, 8, 130);
    addEdge(graph, 8, 26, 580);
    addEdge(graph, 9, 32, 1120);
    addEdge(graph, 9, 12, 830);
    addEdge(graph, 10, 32, 1170);
    addEdge(graph, 11, 15, 800);
    addEdge(graph, 12, 15, 850);
    addEdge(graph, 13, 26, 3900);
    addEdge(graph, 14, 25, 1040);
    addEdge(graph, 14, 21, 250);
    addEdge(graph, 14, 29, 280);
    addEdge(graph, 14, 22, 330);
    addEdge(graph, 15, 16, 640);
    addEdge(graph, 16, 17, 615);
    addEdge(graph, 16, 21, 280);
    addEdge(graph, 17, 31, 340);
    addEdge(graph, 17, 32, 480);
    addEdge(graph, 18, 22, 700);
    addEdge(graph, 18, 23, 490);
    addEdge(graph, 18, 24, 630);
    addEdge(graph, 18, 25, 380);
    addEdge(graph, 18, 27, 475);
    addEdge(graph, 19, 28, 415);
    addEdge(graph, 19, 29, 250);
    addEdge(graph, 19, 31, 250);
    addEdge(graph, 20, 24, 265);
    addEdge(graph, 21, 31, 215);
    addEdge(graph, 22, 28, 240);
    addEdge(graph, 22, 29, 310);
    addEdge(graph, 23, 24, 775);
    addEdge(graph, 23, 27, 440);
    addEdge(graph, 24, 25, 500);
    addEdge(graph, 27, 28, 575);
    addEdge(graph, 29, 31, 200);


    int* distArray = dijkstra(graph, index);


    return distArray;
}
//JNI function to parse between java and C types as well as call the function in the java application
JNIEXPORT jintArray JNICALL
Java_com_gmu_kam_gmuparkingpassoptimizer_CalcResults_getDistances(JNIEnv* env,jobject this, jint buildingIndex) {

    int v = 14;
    jintArray jResults;
    jResults = (*env)->NewIntArray(env, v);
        if(jResults == NULL){
            return NULL;
        }

    jint fill[v];
    int* distances = calc((int)buildingIndex);
    int i;
    for(i = 0; i < v; i++){
        fill[i] = distances[i];
    }

    (*env)->SetIntArrayRegion( env, jResults, 0, v,fill);
    return jResults;
}



