 <!-- TABLERO KANBAN-->
            <v-container>
              <v-card>
                <v-card-title>Tablero KanBan</v-card-title>
                <v-card-text>
                  <v-row>
                    <v-col cols="4">
                      <v-card>
                        <v-card-title>Pendiente</v-card-title>
                        <v-card-text
                          ><v-alert
                            border="top"
                            colored-border
                            color="info"
                            elevation="2"
                          >
                            <div v-for="item in sprint.tasks" :key="item.id">
                              <v-list disabled v-if="item.state == 'Pendiente'">
                                <v-list-item-group>
                                  <v-list-item>
                                    <v-list-item-content>
                                      <v-list-item-title>
                                        <v-icon class="mr-1"
                                          >mdi-clipboard-check-outline</v-icon
                                        >
                                        <strong>{{
                                          item.name
                                        }}</strong></v-list-item-title
                                      >
                                    </v-list-item-content>
                                  </v-list-item>
                                </v-list-item-group>
                              </v-list>
                            </div>
                          </v-alert></v-card-text
                        >
                      </v-card>
                    </v-col>
                    <v-col cols="4">
                      <v-card>
                        <v-card-title>En Curso</v-card-title>
                        <v-card-text
                          ><v-alert
                            border="top"
                            colored-border
                            color="warning"
                            elevation="2"
                          >
                            <div v-for="item in sprint.tasks" :key="item.id">
                              <v-list disabled v-if="item.state == 'En Curso'">
                                <v-list-item-group>
                                  <v-list-item>
                                    <v-list-item-content>
                                      <v-list-item-title>
                                        <v-icon class="mr-1"
                                          >mdi-clipboard-check-outline</v-icon
                                        >
                                        <strong>{{ item.name }}</strong>
                                      </v-list-item-title>
                                    </v-list-item-content>
                                  </v-list-item>
                                </v-list-item-group>
                              </v-list>
                            </div></v-alert
                          ></v-card-text
                        >
                      </v-card> </v-col
                    ><v-col cols="4">
                      <v-card>
                        <v-card-title>Terminado</v-card-title>
                        <v-card-text
                          ><v-alert
                            border="top"
                            colored-border
                            color="success"
                            elevation="2"
                          >
                            <div v-for="item in sprint.tasks" :key="item.id">
                              <v-list disabled v-if="item.state == 'Terminado'">
                                <v-list-item-group>
                                  <v-list-item>
                                    <v-list-item-content>
                                      <v-list-item-title>
                                        <v-icon class="mr-1"
                                          >mdi-clipboard-check-outline</v-icon
                                        ><strong>{{
                                          item.name
                                        }}</strong></v-list-item-title
                                      >
                                    </v-list-item-content>
                                  </v-list-item>
                                </v-list-item-group>
                              </v-list>
                            </div>
                          </v-alert></v-card-text
                        >
                      </v-card>
                    </v-col>
                  </v-row>
                </v-card-text>
              </v-card>
            </v-container>