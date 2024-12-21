from django.urls import path
from mygutenberg import views

urlpatterns = [
    path('books/', views.RedirectionBookList.as_view()),
    path('book/<int:pk>/', views.RedirectionBookDetail.as_view()),
    path('frenchbooks/', views.RedirectionFrenchBookList.as_view()),
    path('frenchbook/<int:pk>/', views.RedirectionBookDetail.as_view()),
    path('englishbooks/', views.RedirectionEnglishBookList.as_view()),
    path('englishbook/<int:pk>/', views.RedirectionBookDetail.as_view()),
]