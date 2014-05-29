class HomeController < ApplicationController
  def index
    p 'hello world'
    render :nothing => true
  end
end
