import threading
import socket
import time
from typing import Dict, Any

SEND_THREAD_NUM = 1
BUFFER_SIZE = 1024


class SendThread(threading.Thread):
    def __init__(self, sock, address, client):
        threading.Thread.__init__(self)
        self.address = address
        self.sock = sock
        self.byte_count = 0
        self.client = client

    def run(self):
        start_time = time.time()
        while time.time() - start_time < 3:
            if self.client.stopped is True:
                break
            self.byte_count += self.sock.sendto(bytes(raw_data, encoding='ascii'), self.address)


class Client(threading.Thread):
    def __init__(self, sock, address, name):
        threading.Thread.__init__(self)
        self.send_threads = []
        self.name = name
        self.stopped = False
        self.start_time = 0
        for index in range(SEND_THREAD_NUM):
            self.send_threads.append(SendThread(sock, address, self))

    def run(self):
        try:
            self.start_time = time.time()
            for thread in self.send_threads:
                thread.start()
            for thread in self.send_threads:
                thread.join()
            print('client: %s, duration: %f' % (self.name, time.time() - self.start_time))
            del running_client[self.name]       # There may be a thread safety issue
        except:
            pass

    def stop(self):
        self.stopped = True


running_client = {}
server_sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
server_sock.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
server_sock.bind(('', 9876))
raw_data = ''
for i in range(BUFFER_SIZE):
    raw_data = raw_data + '0'


if __name__ == '__main__':
    try:
        while True:
            data, client_address = server_sock.recvfrom(BUFFER_SIZE)
            client_name = str(client_address[0]) + ':' + str(client_address[1])
            # print('receive package, data=%s, address=%s' % (data, str(client_address)))

            try:
                data = str(data, encoding='ascii')
                if data == 'stop':
                    if client_name in running_client:
                        running_client[client_name].stop()
                else:
                    raise Exception()
            except:
                client = Client(server_sock, client_address, client_name)
                running_client[client_name] = client
                client.start()
    finally:
        server_sock.close()
